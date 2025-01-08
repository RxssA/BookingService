package ie.atu.bookingservice; // Use dto if moved into a dto package

public class BookingDetailsDTO {
    private String id;
    private double amount;

    // Getters and setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }
}
