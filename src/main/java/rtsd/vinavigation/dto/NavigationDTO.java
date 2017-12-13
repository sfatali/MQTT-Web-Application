package rtsd.vinavigation.dto;

/**
 * Created by Sabina on 11/27/2017.
 */
public class NavigationDTO {
    private int type;
    private String direction;
    private int frequency;
    private int duration;

    public NavigationDTO() {
    }

    public NavigationDTO(int type, int frequency, int duration) {
        this.type = type;
        this.frequency = frequency;
        this.duration = duration;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getDirection() {
        return direction;
    }

    public void setDirection(String direction) {
        this.direction = direction;
    }

    public int getFrequency() {
        return frequency;
    }

    public void setFrequency(int frequency) {
        this.frequency = frequency;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }
}
