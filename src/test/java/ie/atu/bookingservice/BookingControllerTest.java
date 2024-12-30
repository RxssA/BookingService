package ie.atu.bookingservice;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(BookingController.class)
class BookingControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private BookingService bookingService;

    private ObjectMapper objectMapper;
    private BookingDetails bookingDetails;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();
        bookingDetails = new BookingDetails();
        bookingDetails.setId("1");
        bookingDetails.setUserId("User1");
        bookingDetails.setServiceId("Service1");
        bookingDetails.setServiceType("Room");
        bookingDetails.setStatus("AVAILABLE");
        bookingDetails.setBookingDate(new Date());
    }

    @Test
    void createBooking_ShouldReturnCreatedBooking() throws Exception {
        when(bookingService.createBooking(Mockito.any(BookingDetails.class))).thenReturn(bookingDetails);

        mockMvc.perform(post("/api/booking/createBooking")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(bookingDetails)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value("1"))
                .andExpect(jsonPath("$.userId").value("User1"));

        verify(bookingService, times(1)).createBooking(Mockito.any(BookingDetails.class));
    }

    @Test
    void getAvailableBookings_ShouldReturnAvailableBookings() throws Exception {
        List<BookingDetails> availableBookings = Arrays.asList(bookingDetails);
        when(bookingService.getAvailableBookings()).thenReturn(availableBookings);

        mockMvc.perform(get("/api/booking/available")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(1))
                .andExpect(jsonPath("$[0].id").value("1"));

        verify(bookingService, times(1)).getAvailableBookings();
    }

    @Test
    void getAllBookings_ShouldReturnAllBookings() throws Exception {
        List<BookingDetails> allBookings = Arrays.asList(bookingDetails);
        when(bookingService.getAllBookings()).thenReturn(allBookings);

        mockMvc.perform(get("/api/booking")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(1))
                .andExpect(jsonPath("$[0].id").value("1"));

        verify(bookingService, times(1)).getAllBookings();
    }

    @Test
    void getBookingById_ShouldReturnBookingWhenFound() throws Exception {
        when(bookingService.getBookingById("1")).thenReturn(Optional.of(bookingDetails));

        mockMvc.perform(get("/api/booking/getbooking/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value("1"));

        verify(bookingService, times(1)).getBookingById("1");
    }

    @Test
    void getBookingById_ShouldReturnNotFoundWhenNotFound() throws Exception {
        when(bookingService.getBookingById("2")).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/booking/getbooking/2")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());

        verify(bookingService, times(1)).getBookingById("2");
    }

    @Test
    void getBookingByUserId_ShouldReturnBookingsForUser() throws Exception {
        List<BookingDetails> userBookings = Arrays.asList(bookingDetails);
        when(bookingService.getByBookingUserId("User1")).thenReturn(userBookings);

        mockMvc.perform(get("/api/booking/user/User1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(1))
                .andExpect(jsonPath("$[0].id").value("1"));

        verify(bookingService, times(1)).getByBookingUserId("User1");
    }

    @Test
    void updateBooking_ShouldReturnUpdatedBooking() throws Exception {
        BookingDetails updatedBooking = new BookingDetails();
        updatedBooking.setId("1");
        updatedBooking.setUserId("User2");
        updatedBooking.setServiceId("Service2");
        updatedBooking.setServiceType("Room");
        updatedBooking.setStatus("BOOKED");
        updatedBooking.setBookingDate(new Date());

        when(bookingService.updateBooking(eq("1"), Mockito.any(BookingDetails.class))).thenReturn(updatedBooking);

        mockMvc.perform(put("/api/booking/update/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatedBooking)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.userId").value("User2"))
                .andExpect(jsonPath("$.status").value("BOOKED"));

        verify(bookingService, times(1)).updateBooking(eq("1"), Mockito.any(BookingDetails.class));
    }

    @Test
    void updateBooking_ShouldReturnNotFoundWhenNotFound() throws Exception {
        when(bookingService.updateBooking(eq("2"), Mockito.any(BookingDetails.class))).thenReturn(null);

        mockMvc.perform(put("/api/booking/update/2")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(bookingDetails)))
                .andExpect(status().isNotFound());

        verify(bookingService, times(1)).updateBooking(eq("2"), Mockito.any(BookingDetails.class));
    }

    @Test
    void deleteBooking_ShouldReturnOk() throws Exception {
        doNothing().when(bookingService).deleteBooking("1");

        mockMvc.perform(delete("/api/booking/delete/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        verify(bookingService, times(1)).deleteBooking("1");
    }
}