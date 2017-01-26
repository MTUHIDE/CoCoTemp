package space.hideaway.util;

/**
 * Created by dough on 1/24/2017.
 */
public class TemperatureConverterImpl implements TemperatureConverter
{
    /**
     * Converts a string representation of a temperature from celsius format to fahrenheit format.
     * <p>
     * Specifications:
     * Converts the input temperature string to a double, and performs
     * the necessary calculations for conversion to fahrenheit. The output
     * string utilizes the appendSymbol method to insert '°F' at the end of the output.
     *
     * @param celsius The incoming temperature.
     * @return Fahrenheit representation of the input celsius string.
     */
    @Override
    public String celsiusToFahrenheit(String celsius)
    {
        String finishedString = "";

        //TODO implement function here.

        return appendSymbol(TemperatureUnit.FAHRENHEIT, finishedString);
    }

    /**
     * Converts a string representation of a temperature from fahrenheit format to celsius format.
     * <p>
     * Specifications:
     * Converts the input temperature string to a double, and performs
     * the necessary calculations for conversion to celsius. The output
     * string utilizes the appendSymbol method to insert '°C' at the end of the output.
     *
     * @param fahrenheit The incoming temperature.
     * @return Celsius representation of the input Fahrenheit string.
     */
    @Override
    public String fahrenheitToCelsius(String fahrenheit)
    {
        String finishedString = "";

        //TODO implement function here.

        return appendSymbol(TemperatureUnit.CELSIUS, finishedString);
    }

    /**
     * @param temperatureUnit An enum that can either be TemperatureUnit.CELSIUS or TemperatureUnit.FAHRENHEIT.
     * @param temperature     The input temperature string.
     * @return The new temperature string.
     */
    @Override
    public String appendSymbol(TemperatureUnit temperatureUnit, String temperature)
    {
        //TODO implement function here.


        return null;
    }
}
