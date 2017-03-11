package seedu.address.model.datastructure;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.HashSet;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;

/**
 * Testing class for Trie and TrieNode data structure used for auto completion
 */
public class TrieTest {

    private Trie trie;
    private String[] data = {"help", "add", "by", "repeat", "list"
            , "update", "find", "delete", "select", "book"
            , "confirm", "editlabel", "undo", "clear", "push"
            , "pull", "export", "exit", "to", "on"
            , "hourly", "daily", "weekly", "monthly", "yearly"
            , "overdue", "outstanding", "completed", "today", "yesterday"
            , "tomorrow", "bookings"};

    @Before
    public void setup() {
        trie = new Trie();
        for (String line : data) {
            trie.load(line);
        }
    }

    @Test
    public void trie_TestValid_ReturnTrue() {
        testAutocomplete("exp", "export");
    }

    @Test
    public void trie_TestOrderMatchNotMatter_ReturnTrue() {
        //Check if test order matters
        testAutocomplete("ex", "export", "exit");
        testAutocomplete("ex", "exit", "export");
        testAutocomplete("pu", "pull", "push");
    }

    @Test
    public void trie_TestInvalidCommands_ReturnTrue() {
        //Check if test order matters
        testAutocomplete("aNonExistentCommand", "");
        testAutocomplete("", data);
        testAutocomplete("!@#!@!@@@!", "");
    }

    @Test
    public void trie_TestMatchPrefix_ReturnTrue() {
        assertTrue(trie.matchPrefix("ex"));
        assertTrue(trie.matchPrefix(""));
        assertFalse(trie.matchPrefix("nonexistentprefix"));
    }

    @Test
    public void trieNode_TestHashCode() {
        TrieNode node1 = new TrieNode('a');
        TrieNode node2 = new TrieNode('a');
        TrieNode node3 = new TrieNode('b');

        assertTrue(node1.hashCode() == node2.hashCode());
        assertTrue(node1.hashCode() != node3.hashCode());
    }

    @Test
    public void trieNode_TestEquals() {
        TrieNode node1 = new TrieNode('a');
        TrieNode node2 = new TrieNode('a');
        TrieNode node3 = new TrieNode('b');

        assertTrue(node1.equals(node2));
        assertFalse(node1.equals(node3));
        assertFalse(node1.equals(null));
    }

    @Test
    public void trieNode_TestContains() {
        TrieNode node = new TrieNode('a');
        assertFalse(node.contains('b')); //Should not contain

        node.add('b');
        assertTrue(node.contains('b'));
        assertFalse(node.contains('\0'));
    }

    @Test
    public void trieNode_TestKeySet_ReturnTrue() {
        //Nodes initialized should have no children nodes
        TrieNode node1 = new TrieNode('a');
        TrieNode node2 = new TrieNode('b');
        assertTrue(node1.getChildrenNodeValues().containsAll(node2.getChildrenNodeValues()));
    }

    public void testAutocomplete(String testString, String... expected) {
        Set<String> actual = new HashSet<String>(trie.findCompletions(testString));
        Set<String> expectedList = new HashSet<String>();
        for (String expectedString : expected) {
            expectedList.add(expectedString);
        }
        assertTrue(expectedList.containsAll(actual));
    }

}