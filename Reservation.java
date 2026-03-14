package model;

public class Reservation {
    // ...existing code...
	 private int id;
	    private User user;
	    private Room room;
	    private String date;
	    private String startTime;
	    private String endTime;
	    private ReservationStatus status;

	    public Reservation(int id, User user, Room room, String date, String startTime, String endTime) {
	        this.id = id;
	        this.user = user;
	        this.room = room;
	        this.date = date;
	        this.startTime = startTime;
	        this.endTime = endTime;
	        this.status = ReservationStatus.EN_ATTENTE;
	    }

	    public int getId() {
	        return id;
	    }

	    public User getUser() {
	        return user;
	    }

	    public Room getRoom() {
	        return room;
	    }

	    public String getDate() {
	        return date;
	    }

	    public String getStartTime() {
	        return startTime;
	    }

	    public String getEndTime() {
	        return endTime;
	    }

	    public ReservationStatus getStatus() {
	        return status;
	    }

	    public void setStatus(ReservationStatus status) {
	        this.status = status;
	    }
	
}
