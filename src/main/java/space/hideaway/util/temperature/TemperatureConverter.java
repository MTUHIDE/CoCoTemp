package space.hideaway.util.temperature;

/**
 * Created by dough on 1/24/2017.
 */
public interface TemperatureConverter
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
    String celsiusToFahrenheit(String celsius);

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
    String fahrenheitToCelsius(String fahrenheit);


    /**
     * Appends the correct temperature unit symbol and degree symbol at the end of a string.
     * Eg. °F or °C
     *
     * @param temperatureUnit An enum that can either be TemperatureUnit.CELSIUS or TemperatureUnit.FAHRENHEIT.
     * @param temperature     The input temperature string.
     * @ The new temperature string.
     */
    String appendSymbol(TemperatureUnit temperatureUnit, String temperature);
}
