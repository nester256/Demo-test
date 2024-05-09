package com.example.demo.service.impl.specification;

import com.example.demo.model.Services;
import com.example.demo.model.filter.ServiceFilter;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Objects;

@Component
public class ServicesSpecification {
    private static final String LIKE_SIGN = "%";
    public Specification<Services> byFilter(ServiceFilter filter) {
        return Objects.isNull(filter)? null : Specification.where(byIds(filter.getIds()))
                .or(bySearchLike(filter.getSearch(), Services.Fields.name))
                .or(bySearchLike(filter.getSearch(), Services.Fields.language));
    }

    private Specification<Services> bySearchLike(String search, String fieldName) {
        return StringUtils.isBlank(search)? null : (root, query, cb) -> cb.like(
                cb.lower(root.get(fieldName)),
                cb.lower(cb.literal(String.join(StringUtils.EMPTY, LIKE_SIGN, search, LIKE_SIGN)))
        );
    }

    private Specification<Services> byIds(List<Long> ids) {
        return CollectionUtils.isEmpty(ids)? null : (root, query, cb) -> root.get(Services.Fields.id).in(ids);
    }
}
