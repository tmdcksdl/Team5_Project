package com.example.team5_project.repository;

import com.example.team5_project.dto.product.response.PageableProductResponse;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.SliceImpl;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

import static com.example.team5_project.entity.QProduct.product;
import static com.example.team5_project.entity.QStore.store;

@Repository
@RequiredArgsConstructor
public class ProductQueryRepository {

    private final JPAQueryFactory queryFactory;

    //최적화 전
    public Page<PageableProductResponse> findByPriceRange1( Integer min, Integer max, Pageable pageable) {

        Integer minPrice = (min != null) ? min : 0;
        Integer maxPrice = (max != null) ? max : queryFactory.select(product.price.max()).from(product).fetchOne();


        List<PageableProductResponse> content = queryFactory
                .select(Projections.constructor(PageableProductResponse.class,
                        store.name, product.name, product.price))
                .from(product)
                .join(product.store, store)
                .where(product.price.between(minPrice, maxPrice))
                .orderBy(product.price.desc(), product.name.asc())
                .limit(pageable.getPageSize())
                .offset(pageable.getOffset())
                .fetch();

        Long totalCount = queryFactory
                .select(product.count())
                .from(product)
                .where(product.price.between(minPrice, maxPrice))
                .fetchOne();

        long total = Optional.ofNullable(totalCount).orElse(0L);

        return new PageImpl<>(content, pageable, total);
    }


    //최적화 후
    public SliceImpl<PageableProductResponse> findByPriceRange(Integer min, Integer max, Pageable pageable) {
        BooleanBuilder builder = new BooleanBuilder();

        if (min != null) {
            builder.and(product.price.goe(min));
        }
        if (max != null) {
            builder.and(product.price.loe(max));
        }

        List<PageableProductResponse> response = queryFactory
                .select(Projections.constructor(PageableProductResponse.class,
                        store.name, product.name, product.price)
                )
                .from(product)
                .join(product.store)
                .where(builder)
                .orderBy(product.price.desc(), product.name.asc())
                .limit(pageable.getPageSize()+1)
                .offset(pageable.getPageNumber())
                .fetch();

        boolean hasNext = response.size() > pageable.getPageSize();

        if (hasNext) {
            response.remove(response.size() - 1);
        }

        return new SliceImpl<>(response, pageable, hasNext);
    }
}
