package com.gyan.pg_management.service.booking;

import com.gyan.pg_management.dto.request.booking.BookingCancelRequest;
import com.gyan.pg_management.dto.request.booking.BookingCheckoutRequest;
import com.gyan.pg_management.dto.request.booking.BookingCreateRequest;
import com.gyan.pg_management.dto.response.booking.BookingResponse;
import com.gyan.pg_management.entity.Bed;
import com.gyan.pg_management.entity.Booking;
import com.gyan.pg_management.entity.User;
import com.gyan.pg_management.enums.BedStatus;
import com.gyan.pg_management.enums.BookingStatus;
import com.gyan.pg_management.repository.BedRepository;
import com.gyan.pg_management.repository.BookingRepository;
import com.gyan.pg_management.repository.UserRepository;
import com.gyan.pg_management.service.balance.BalanceService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BookingServiceImplTest {

    @Mock
    private BookingRepository bookingRepository;
    @Mock
    private BalanceService balanceService;
    @Mock
    private BedRepository bedRepository;
    @Mock
    private UserRepository userRepository;
    @InjectMocks
    private BookingServiceImpl bookingService;
    @Captor
    private ArgumentCaptor<Booking> bookingCaptor;

    private User tenant;
    private Bed bed;

    @BeforeEach
    void setUp() {
        tenant = User.builder().id(10L).build();
        bed = Bed.builder().id(20L).status(BedStatus.OCCUPIED).build();
    }

    @Test
    void createBookingBuildsAndReturnsResponse() {
        BookingCreateRequest request = new BookingCreateRequest(
                tenant.getId(),
                bed.getId(),
                LocalDate.now().plusDays(1),
                12000.0,
                5000.0
        );
        when(bedRepository.findById(bed.getId())).thenReturn(Optional.of(bed));
        when(userRepository.findById(tenant.getId())).thenReturn(Optional.of(tenant));
        when(bookingRepository.findByBedAndStatus(bed, BookingStatus.ACTIVE)).thenReturn(Optional.empty());
        when(bookingRepository.findByTenantAndStatus(tenant, BookingStatus.ACTIVE)).thenReturn(Optional.empty());

        Booking savedBooking = Booking.builder()
                .id(55L)
                .tenant(tenant)
                .bed(bed)
                .startDate(request.getStartDate())
                .monthlyRent(request.getMonthlyRent())
                .securityDeposit(request.getSecurityDeposit())
                .status(BookingStatus.ACTIVE)
                .build();
        when(bookingRepository.save(any(Booking.class))).thenReturn(savedBooking);

        BookingResponse response = bookingService.createBooking(request);

        verify(bookingRepository).save(bookingCaptor.capture());
        Booking captured = bookingCaptor.getValue();
        assertThat(captured.getTenant()).isEqualTo(tenant);
        assertThat(captured.getBed()).isEqualTo(bed);
        assertThat(captured.getMonthlyRent()).isEqualTo(request.getMonthlyRent());
        assertThat(response.getBookingId()).isEqualTo(savedBooking.getId());
        assertThat(response.getStatus()).isEqualTo(BookingStatus.ACTIVE);
    }

    @Test
    void createBookingRejectsOccupiedBed() {
        BookingCreateRequest request = new BookingCreateRequest(
                tenant.getId(),
                bed.getId(),
                LocalDate.now().plusDays(1),
                12000.0,
                5000.0
        );
        when(bedRepository.findById(bed.getId())).thenReturn(Optional.of(bed));
        when(userRepository.findById(tenant.getId())).thenReturn(Optional.of(tenant));
        when(bookingRepository.findByBedAndStatus(bed, BookingStatus.ACTIVE))
                .thenReturn(Optional.of(Booking.builder().id(1L).build()));

        assertThatThrownBy(() -> bookingService.createBooking(request))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("Bed is already occupied");
    }

    @Test
    void createBookingRejectsTenantWithActiveBooking() {
        BookingCreateRequest request = new BookingCreateRequest(
                tenant.getId(),
                bed.getId(),
                LocalDate.now().plusDays(1),
                12000.0,
                5000.0
        );
        when(bedRepository.findById(bed.getId())).thenReturn(Optional.of(bed));
        when(userRepository.findById(tenant.getId())).thenReturn(Optional.of(tenant));
        when(bookingRepository.findByBedAndStatus(bed, BookingStatus.ACTIVE)).thenReturn(Optional.empty());
        when(bookingRepository.findByTenantAndStatus(tenant, BookingStatus.ACTIVE))
                .thenReturn(Optional.of(Booking.builder().id(2L).build()));

        assertThatThrownBy(() -> bookingService.createBooking(request))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("Tenant already has an active booking");
    }

    @Test
    void checkoutBookingCompletesWhenNoDues() {
        Booking booking = Booking.builder()
                .id(88L)
                .tenant(tenant)
                .bed(bed)
                .status(BookingStatus.ACTIVE)
                .build();
        BookingCheckoutRequest request = new BookingCheckoutRequest(booking.getId(), LocalDate.now().plusDays(3));
        when(bookingRepository.findById(booking.getId())).thenReturn(Optional.of(booking));
        when(balanceService.hasPendingDues(tenant)).thenReturn(false);
        when(bookingRepository.save(booking)).thenReturn(booking);

        BookingResponse response = bookingService.checkoutBooking(request);

        assertThat(response.getStatus()).isEqualTo(BookingStatus.COMPLETED);
        assertThat(booking.getEndDate()).isEqualTo(request.getCheckoutDate());
        assertThat(booking.getBed().getStatus()).isEqualTo(BedStatus.VACANT);
    }

    @Test
    void checkoutBookingBlocksWhenDuesExist() {
        Booking booking = Booking.builder()
                .id(88L)
                .tenant(tenant)
                .bed(bed)
                .status(BookingStatus.ACTIVE)
                .build();
        BookingCheckoutRequest request = new BookingCheckoutRequest(booking.getId(), LocalDate.now().plusDays(3));
        when(bookingRepository.findById(booking.getId())).thenReturn(Optional.of(booking));
        when(balanceService.hasPendingDues(tenant)).thenReturn(true);

        assertThatThrownBy(() -> bookingService.checkoutBooking(request))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("Outstanding dues");
    }

    @Test
    void cancelBookingRejectsWhenNotActive() {
        Booking booking = Booking.builder()
                .id(99L)
                .status(BookingStatus.COMPLETED)
                .startDate(LocalDate.now().plusDays(2))
                .build();
        BookingCancelRequest request = new BookingCancelRequest(booking.getId());
        when(bookingRepository.findById(booking.getId())).thenReturn(Optional.of(booking));

        assertThatThrownBy(() -> bookingService.cancelBooking(request))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("Only active bookings can be cancelled");
    }

    @Test
    void cancelBookingRejectsWhenStayStarted() {
        Booking booking = Booking.builder()
                .id(100L)
                .status(BookingStatus.ACTIVE)
                .startDate(LocalDate.now())
                .build();
        BookingCancelRequest request = new BookingCancelRequest(booking.getId());
        when(bookingRepository.findById(booking.getId())).thenReturn(Optional.of(booking));

        assertThatThrownBy(() -> bookingService.cancelBooking(request))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("Stay has already started");
    }

    @Test
    void cancelBookingUpdatesStatusAndEndDate() {
        Booking booking = Booking.builder()
                .id(101L)
                .status(BookingStatus.ACTIVE)
                .startDate(LocalDate.now().plusDays(5))
                .build();
        BookingCancelRequest request = new BookingCancelRequest(booking.getId());
        when(bookingRepository.findById(booking.getId())).thenReturn(Optional.of(booking));
        when(bookingRepository.save(booking)).thenReturn(booking);

        BookingResponse response = bookingService.cancelBooking(request);

        assertThat(response.getStatus()).isEqualTo(BookingStatus.CANCELLED);
        assertThat(booking.getEndDate()).isEqualTo(LocalDate.now());
    }
}
