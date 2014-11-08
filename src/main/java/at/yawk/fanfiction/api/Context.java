package at.yawk.fanfiction.api;

import java.io.IOException;
import java.util.List;
import org.xml.sax.SAXException;

/**
 * @author yawkat
 */
public interface Context {
    List<SubCategory> getSubCategories(Category category, boolean crossover) throws Exception;

    SubCategory getSubCategoryInfo(SubCategory subCategory) throws Exception;

    SearchResult search(String url) throws Exception;

    Chapter getChapter(Story story, int chapter) throws Exception;
}
