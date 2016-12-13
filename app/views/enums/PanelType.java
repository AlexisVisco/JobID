package views.enums;

/**
 * Par Alexis le 20/11/2016.
 */
public enum PanelType
{
    SUCCESS("success"),
    DANGER("danger"),
    WARNING("warning"),
    INFO("info");

    public String value;

    PanelType(String value)
    {
        this.value = value;
    }


}
