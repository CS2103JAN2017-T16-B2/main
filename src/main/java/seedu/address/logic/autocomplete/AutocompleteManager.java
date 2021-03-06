package seedu.address.logic.autocomplete;

import java.util.List;
import java.util.logging.Logger;

import seedu.address.commons.core.LogsCenter;
import seedu.address.model.datastructure.AutocompleteDataStructure;
import seedu.address.model.datastructure.Trie;

//@@author A0140042A
/**
 * Handles the auto-completion of the input
 */
public class AutocompleteManager {
    private final Logger logger = LogsCenter.getLogger(AutocompleteManager.class);
    public static final String[] AUTOCOMPLETE_DATA = { "add", "book", "bookings", "by", "change"
            , "clear", "completed", "confirm", "days"
            , "delete", "edit", "editbooking", "every", "exit"
            , "find", "from", "incomplete", "list"
            , "load", "mark", "months", "on"
            , "outstanding", "overdue", "remove", "repeat", "saveas"
            , "select", "to", "today", "tomorrow", "undo", "unmark"
            , "years", "yesterday", "help" };

    private AutocompleteDataStructure data;

    /**
     * Initializes auto-complete object with default auto-complete data
     */
    public AutocompleteManager() {
        this(AUTOCOMPLETE_DATA);
    }

    /**
     * Initializes auto-complete object with specified auto-complete data
     */
    public AutocompleteManager(String... data) {
        assert data != null;
        this.data = new Trie();
        addData(data);
    }

    /**
     * Adds more strings for auto completion
     */
    public void addData(String... phrases) {
        assert phrases != null;
        data.load(phrases);
    }

    /**
     * Find auto-complete suggestions given a search term
     * @param term - search term
     * @return a list of suggestions for the given term
     */
    public AutocompleteResponse getSuggestions(AutocompleteRequest request) {
        logger.info("Received request (" + request + ")");
        String wordAtCursor = getWordAtCursor(request);
        List<String> suggestions = data.findCompletions(wordAtCursor);
        AutocompleteResponse response = new AutocompleteResponse(request, suggestions);
        return processRequest(response);
    }

    /**
     * Generates a new AutocompleteResponse with the new phrase, caretPosition and list of suggestions
     */
    private AutocompleteResponse processRequest(AutocompleteResponse response) {
        //if empty or matches all (AKA no match)
        if (response.getSuggestions().isEmpty() ||
                response.getSuggestions().size() == AutocompleteManager.AUTOCOMPLETE_DATA.length) {
            return response;
        }

        logger.info("Suggestions: " + response.getSuggestions());

        String commonSubstring = getLongestCommonSubString(response);

        int cursorWordEndIndex = getEndIndexOfWordAtCursor(response);
        String appendCharacter = "";
        boolean endHasSpace = false;
        //Append a space IF AND ONLY IF the auto-completed word is the last word of the command
        //and there is only 1 suggestion
        if (cursorWordEndIndex == response.getPhrase().trim().length() && response.getSuggestions().size() == 1) {
            //Append a space if there is a space at the end already
            appendCharacter = padWithSpace(response);
            endHasSpace = true;
        }

        //Move position caret to after auto completed word
        int newPositionCaret = getNewPositionCaret(response, commonSubstring, cursorWordEndIndex, endHasSpace);

        response.setPhrase(replacePhraseWithSuggestion(response, commonSubstring, appendCharacter));
        response.setCaretPosition(newPositionCaret);
        return response;
    }

    /**
     * Returns the longest command substring from the suggestions given in the response
     */
    private String getLongestCommonSubString(AutocompleteResponse response) {
        String longestString = getLongestString(response.getSuggestions());
        int commonSubstringIndex = getCommonSubstringEndIndexFromStart(response.getSuggestions());
        String commonSubstring = longestString.substring(0, commonSubstringIndex);
        return commonSubstring;
    }

    /**
     * Returns the new position caret after suggestion replacement
     */
    private int getNewPositionCaret(AutocompleteResponse response, String commonSubstring, int cursorWordEndIndex,
            boolean endHasSpace) {
        String currentWord = getWordAtCursor(response);
        int newPositionCaret = cursorWordEndIndex
                               - currentWord.length()
                               + commonSubstring.length()
                               + (endHasSpace ? 1 : 0);
        return newPositionCaret;
    }

    /**
     * Returns a string that contains a space if the phrase at the end does not contain a space character
     */
    private String padWithSpace(AutocompleteResponse response) {
        String appendCharacter = "";
        if (response.getPhrase().charAt(response.getPhrase().length() - 1) != ' ') {
            appendCharacter = " ";
        }
        return appendCharacter;
    }

    /**
     * Replaces the phrase's word at the caret position with the suggestion provided
     * Appends toAppend if there is any character to append after that
     */
    private String replacePhraseWithSuggestion(AutocompleteResponse response, String suggestion, String toAppend) {
        StringBuffer commandAutocompleted = new StringBuffer(response.getPhrase());

        commandAutocompleted.replace(getStartIndexOfWordAtCursor(response),
                                        getEndIndexOfWordAtCursor(response),
                                        (suggestion + toAppend));

        return commandAutocompleted.toString();
    }


    /**
     * Returns the character index at which the characters in the list of suggestions start to differ
     */
    private int getCommonSubstringEndIndexFromStart(List<String> suggestions) {
        String longestString = getLongestString(suggestions);
        int commonSubstringIndex = 0;
        char currentChar;
        commonSubstring:
        for (int charIndex = 0; charIndex < longestString.length(); charIndex++) {
            currentChar = longestString.charAt(charIndex);
            for (String suggestion : suggestions) {
                if (suggestion.length() <= charIndex || suggestion.charAt(charIndex) != currentChar) {
                    break commonSubstring;
                }
            }
            commonSubstringIndex++;
        }
        return commonSubstringIndex;
    }

    /**
     * Returns the longest string in the list, empty string otherwise
     */
    private String getLongestString(List<String> suggestions) {
        String longest = "";
        for (String suggestion : suggestions) {
            if (suggestion.length() >= longest.length()) {
                longest = suggestion;
            }
        }
        return longest;
    }

    /**
     * Extracts the word at the current the caretPosition in commandTextField
     */
    private String getWordAtCursor(AutocompleteRequest request) {
        if (request.getPhrase() != null && !request.getPhrase().trim().equals("")) {
            int startIndex = getStartIndexOfWordAtCursor(request);
            int endIndex = getEndIndexOfWordAtCursor(request);
            return request.getPhrase().substring(startIndex, endIndex);
        } else {
            return request.getPhrase();
        }

    }

    /**
     * Gets the start index of the phrase at the caretPosition of the request
     */
    private int getStartIndexOfWordAtCursor(AutocompleteRequest request) {
        if (request.getPhrase().isEmpty()) {
            return 0;
        }

        int currentPosition = request.getCaretPosition();
        int startIndex;
        currentPosition = currentPosition - 1 < 0 ? 0 : currentPosition - 1;
        while (currentPosition > 0  &&
                currentPosition < request.getPhrase().length() &&
                !Character.isWhitespace(request.getPhrase().charAt(currentPosition))) {
            currentPosition--;
        }

        //Increment index by 1 if whitespace is met
        if (Character.isWhitespace(request.getPhrase().charAt(currentPosition))) {
            currentPosition++;
        }

        startIndex = currentPosition;
        return startIndex;
    }

    /**
     * Gets the end index of the phrase at the caretPosition of the request
     */
    private int getEndIndexOfWordAtCursor(AutocompleteRequest request) {
        if (request.getPhrase().isEmpty()) {
            return 0;
        }

        int currentPosition = request.getCaretPosition();
        int endIndex;
        while (currentPosition >= 0  &&
                currentPosition < request.getPhrase().length() &&
                !Character.isWhitespace(request.getPhrase().charAt(currentPosition))) {
            currentPosition++;
        }
        endIndex = currentPosition;
        return endIndex;
    }

    @Override
    public boolean equals(Object other) {
        if (other instanceof AutocompleteManager) {
            AutocompleteManager compare = (AutocompleteManager) other;
            AutocompleteRequest request = new AutocompleteRequest("", 0);
            return getSuggestions(request).equals(compare.getSuggestions(request));
        } else {
            return false;
        }
    }

}
