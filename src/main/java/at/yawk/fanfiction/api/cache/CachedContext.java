package at.yawk.fanfiction.api.cache;

import at.yawk.fanfiction.api.*;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.Value;

/**
 * @author yawkat
 */
@RequiredArgsConstructor
public class CachedContext implements Context {
    private final Context delegate;

    private final LoadingCache<CategoryWrapper, List<SubCategory>> subCategories =
            CacheBuilder.newBuilder().build(new CacheLoader<CategoryWrapper, List<SubCategory>>() {
                @Override
                public List<SubCategory> load(CategoryWrapper key) throws Exception {
                    return delegate.getSubCategories(key.category, key.crossover);
                }
            });

    private final LoadingCache<SubCategoryWrapper, SubCategory> subCategoryInfo =
            CacheBuilder.newBuilder().build(new CacheLoader<SubCategoryWrapper, SubCategory>() {
                @Override
                public SubCategory load(SubCategoryWrapper key) throws Exception {
                    SubCategory sub = SubCategory.builder()
                                    .category(key.category.category)
                                    .crossover(key.category.crossover)
                                    .name(key.name)
                                    .build();
                    return delegate.getSubCategoryInfo(sub);
                }
            });

    @Override
    public List<SubCategory> getSubCategories(Category category, boolean crossover) throws Exception {
        return subCategories.get(new CategoryWrapper(category, crossover));
    }

    @Override
    public SubCategory getSubCategoryInfo(SubCategory subCategory) throws Exception {
        return subCategoryInfo.get(new SubCategoryWrapper(
                new CategoryWrapper(subCategory.getCategory(), subCategory.isCrossover()),
                subCategory.getName()
        ));
    }

    @Override
    public SearchResult search(String url) throws Exception {
        return delegate.search(url);
    }

    @Override
    public Chapter getChapter(Story story, int chapter) throws Exception {
        return delegate.getChapter(story, chapter);
    }

    @Value
    private static class CategoryWrapper {
        Category category;
        boolean crossover;
    }

    @Value
    private static class SubCategoryWrapper {
        CategoryWrapper category;
        String name;
    }
}
