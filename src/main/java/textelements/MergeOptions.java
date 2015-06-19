package textelements;

/**
 * Options for merging text elements.
 * Depending on the option, there can be spaces to the left or right of
 * the element, or these spaces should collapse.
 */
public enum MergeOptions {
    NO_OPERATION, MERGE_LEFT, MERGE_RIGHT, MERGE_BOTH;
}
