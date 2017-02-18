package space.hideaway.model;

/**
 * Created by dough on 2017-02-18.
 */
public class UploadFormWrapper
{
    private Device selectedDevice;
    private String description;

    public Device getSelectedDevice()
    {
        return selectedDevice;
    }

    public void setSelectedDevice(Device selectedDevice)
    {
        this.selectedDevice = selectedDevice;
    }

    public String getDescription()
    {
        return description;
    }

    public void setDescription(String description)
    {
        this.description = description;
    }
}
