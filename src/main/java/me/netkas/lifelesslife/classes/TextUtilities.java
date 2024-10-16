package me.netkas.lifelesslife.classes;

public final class TextUtilities
{
    /**
     * Normalizes the input sentence by converting it to lowercase and
     * then capitalizing the first letter of each sentence.
     *
     * @param input the input sentence to be normalized. May be null or empty.
     * @return the normalized sentence with proper capitalization or the
     *         original input if it is null or empty.
     */
    public static String normalizeSentence(String input)
    {
        if (input == null || input.isEmpty())
        {
            return input;
        }

        input = input.toLowerCase();
        char[] charArray = input.toCharArray();
        boolean capitalizeNext = true;

        for (int i = 0; i < charArray.length; i++)
        {
            if (capitalizeNext && Character.isLetter(charArray[i]))
            {
                charArray[i] = Character.toUpperCase(charArray[i]);
                capitalizeNext = false;
            }
            else if (charArray[i] == '.' || charArray[i] == '!' || charArray[i] == '?')
            {
                capitalizeNext = true;
            }
        }

        return new String(charArray);
    }

    /**
     * Capitalizes the first letter of each word in the given input string.
     *
     * @param input The string whose title needs to be capitalized. If null or empty, returns the input as is.
     * @return A new string with the first letter of each word capitalized. Words are separated by one or more whitespace characters.
     */
    public static String capitalizeTitle(String input)
    {
        if (input == null || input.isEmpty())
        {
            return input; // Handle null and empty string cases
        }

        String[] words = input.toLowerCase().split("\\s+");
        StringBuilder result = new StringBuilder();

        for (String word : words)
        {
            if (!word.isEmpty())
            {
                result.append(Character.toUpperCase(word.charAt(0))).append(word.substring(1)).append(" ");
            }
        }

        return result.toString().trim();
    }
}
