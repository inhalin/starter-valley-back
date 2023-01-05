package startervalley.backend.util;

public class PaginationConstants {

    public static final String DEFAULT_PAGE_NUMBER = "1";
    public static final String DEFAULT_PAGE_SIZE = "10";
    public static final String DEFAULT_SORT_BY = "id";
    public static final String DEFAULT_SORT_DIRECTION = "desc";

    public static final int MINIMUM_PAGE_NUMBER = 1;
    public static final int[] ALLOWED_PAGE_SIZE = {5, 10, 30, 50, 100};
}
