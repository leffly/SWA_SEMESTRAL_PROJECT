package cz.cvut.fel.swa.book.request;

public class BookRequest {

    private String genre;
    private int maxPriceLimit;
    private int minPriceLimit;

    public BookRequest() {
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public int getMaxPriceLimit() {
        return maxPriceLimit;
    }

    public void setMaxPriceLimit(int maxPriceLimit) {
        this.maxPriceLimit = maxPriceLimit;
    }

    public int getMinPriceLimit() {
        return minPriceLimit;
    }

    public void setMinPriceLimit(int minPriceLimit) {
        this.minPriceLimit = minPriceLimit;
    }
}
