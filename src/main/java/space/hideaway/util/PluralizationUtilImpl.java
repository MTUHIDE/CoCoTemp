package space.hideaway.util;

/**
 * Created by dough on 1/24/2017.
 */
public class PluralizationUtilImpl implements PluralizationUtil
{
    /**
     * Adds an 's' if n is a number that occurs with a plural form of a word.
     * Examples:
     * 1 upload
     * 2 uploads
     * <p>
     * 1 record
     * 2 records
     * <p>
     * 1 site
     * 2 sites
     * <p>
     * Should also return the plural form of a word if n is zero.
     * Correct output is 'n [word]' where n is the integer supplied as an argument
     * and word is the word supplied. There is no need to worry about words that have plural edge cases
     * such as 'supplies' or puppies.
     *
     * @param n      The integer value of the supposed item.
     * @param string The item text/description/name
     * @return The plural or singular form of the input string.
     */
    @Override
    public String pluralize(int n, String string)
    {
        return null;
    }
}
