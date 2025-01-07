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

import java.util.Date;

import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
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
                        .content(objectMapper.writeValueAsString(bookingDetails))
                        .with(csrf()) // Add CSRF token
                        .with(user("mockUser").roles("USER"))) // Mock user authentication
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value("1"))
                .andExpect(jsonPath("$.userId").value("User1"));

        verify(bookingService, times(1)).createBooking(Mockito.any(BookingDetails.class));
    }

    @Test
    void confirmBooking_ShouldReturnBookingNotConfirmed() throws Exception {
        bookingDetails.setToken("mock-token"); // Mock valid token

        when(bookingService.confirmBooking(bookingDetails.getId())).thenReturn(false);

        mockMvc.perform(post("/api/booking/confirmBooking")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(bookingDetails))
                        .with(csrf()) // Add CSRF token
                        .with(user("mockUser").roles("USER"))) // Mock user authentication
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Booking could not be confirmed."));

        verify(bookingService, times(1)).confirmBooking(bookingDetails.getId());
    }

    @Test
    void confirmBooking_ShouldReturnBookingConfirmed() throws Exception {
        bookingDetails.setToken("mock-token"); // Mock valid token

        when(bookingService.confirmBooking(bookingDetails.getId())).thenReturn(true);

        mockMvc.perform(post("/api/booking/confirmBooking")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(bookingDetails))
                        .with(csrf()) // Add CSRF token
                        .with(user("mockUser").roles("USER"))) // Mock user authentication
                .andExpect(status().isOk())
                .andExpect(content().string("Booking confirmed successfully."));

        verify(bookingService, times(1)).confirmBooking(bookingDetails.getId());
    }

    @Test
    void confirmBooking_ShouldReturnUnauthorizedIfInvalidToken() throws Exception {
        bookingDetails.setToken("invalid-token"); // Mock invalid token

        mockMvc.perform(post("/api/booking/confirmBooking")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(bookingDetails))
                        .with(csrf()) // Add CSRF token
                        .with(user("mockUser").roles("USER"))) // Mock user authentication
                .andExpect(status().isUnauthorized())
                .andExpect(content().string("Invalid or expired auth token."));
    }

    @Test
    void deleteBooking_ShouldReturnOk() throws Exception {
        doNothing().when(bookingService).deleteBooking("1");

        mockMvc.perform(delete("/api/booking/delete/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .with(csrf()) // Add CSRF token
                        .with(user("mockUser").roles("USER"))) // Mock user authentication
                .andExpect(status().isOk());

        verify(bookingService, times(1)).deleteBooking("1");
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
                        .content(objectMapper.writeValueAsString(updatedBooking))
                        .with(csrf()) // Add CSRF token
                        .with(user("mockUser").roles("USER"))) // Mock user authentication
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.userId").value("User2"))
                .andExpect(jsonPath("$.status").value("BOOKED"));

        verify(bookingService, times(1)).updateBooking(eq("1"), Mockito.any(BookingDetails.class));
    }
}
