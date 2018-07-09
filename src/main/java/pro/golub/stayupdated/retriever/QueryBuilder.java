package pro.golub.stayupdated.retriever;

import twitter4j.Query;

public class QueryBuilder {

    public static final String LANG = "en";
    public static final int COUNT = 10;

    static Query buildQuery(String hashTag) {
        twitter4j.Query query = new twitter4j.Query(buildHashTag(hashTag));
        query.setLang(LANG);
        query.setResultType(twitter4j.Query.ResultType.popular);
        query.setCount(COUNT);
        return query;
    }

    private static String buildHashTag(String hashTag) {
        StringBuilder sb = new StringBuilder();
        sb.append("\"#").append(hashTag).append("\"");
        return sb.toString();
    }


}
