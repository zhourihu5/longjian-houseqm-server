package com.longfor.longjian.houseqm.po.stat;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class StatHouseQmProjectDailyStatExample {
    protected String orderByClause;

    protected boolean distinct;

    protected List<Criteria> oredCriteria;

    public StatHouseQmProjectDailyStatExample() {
        oredCriteria = new ArrayList<Criteria>();
    }

    public void setOrderByClause(String orderByClause) {
        this.orderByClause = orderByClause;
    }

    public String getOrderByClause() {
        return orderByClause;
    }

    public void setDistinct(boolean distinct) {
        this.distinct = distinct;
    }

    public boolean isDistinct() {
        return distinct;
    }

    public List<Criteria> getOredCriteria() {
        return oredCriteria;
    }

    public void or(Criteria criteria) {
        oredCriteria.add(criteria);
    }

    public Criteria or() {
        Criteria criteria = createCriteriaInternal();
        oredCriteria.add(criteria);
        return criteria;
    }

    public Criteria createCriteria() {
        Criteria criteria = createCriteriaInternal();
        if (oredCriteria.size() == 0) {
            oredCriteria.add(criteria);
        }
        return criteria;
    }

    protected Criteria createCriteriaInternal() {
        Criteria criteria = new Criteria();
        return criteria;
    }

    public void clear() {
        oredCriteria.clear();
        orderByClause = null;
        distinct = false;
    }

    protected abstract static class GeneratedCriteria {
        protected List<Criterion> criteria;

        protected GeneratedCriteria() {
            super();
            criteria = new ArrayList<Criterion>();
        }

        public boolean isValid() {
            return criteria.size() > 0;
        }

        public List<Criterion> getAllCriteria() {
            return criteria;
        }

        public List<Criterion> getCriteria() {
            return criteria;
        }

        protected void addCriterion(String condition) {
            if (condition == null) {
                throw new RuntimeException("Value for condition cannot be null");
            }
            criteria.add(new Criterion(condition));
        }

        protected void addCriterion(String condition, Object value, String property) {
            if (value == null) {
                throw new RuntimeException("Value for " + property + " cannot be null");
            }
            criteria.add(new Criterion(condition, value));
        }

        protected void addCriterion(String condition, Object value1, Object value2, String property) {
            if (value1 == null || value2 == null) {
                throw new RuntimeException("Between values for " + property + " cannot be null");
            }
            criteria.add(new Criterion(condition, value1, value2));
        }

        public Criteria andIdIsNull() {
            addCriterion("id is null");
            return (Criteria) this;
        }

        public Criteria andIdIsNotNull() {
            addCriterion("id is not null");
            return (Criteria) this;
        }

        public Criteria andIdEqualTo(Integer value) {
            addCriterion("id =", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdNotEqualTo(Integer value) {
            addCriterion("id <>", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdGreaterThan(Integer value) {
            addCriterion("id >", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdGreaterThanOrEqualTo(Integer value) {
            addCriterion("id >=", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdLessThan(Integer value) {
            addCriterion("id <", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdLessThanOrEqualTo(Integer value) {
            addCriterion("id <=", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdIn(List<Integer> values) {
            addCriterion("id in", values, "id");
            return (Criteria) this;
        }

        public Criteria andIdNotIn(List<Integer> values) {
            addCriterion("id not in", values, "id");
            return (Criteria) this;
        }

        public Criteria andIdBetween(Integer value1, Integer value2) {
            addCriterion("id between", value1, value2, "id");
            return (Criteria) this;
        }

        public Criteria andIdNotBetween(Integer value1, Integer value2) {
            addCriterion("id not between", value1, value2, "id");
            return (Criteria) this;
        }

        public Criteria andProjectIdIsNull() {
            addCriterion("project_id is null");
            return (Criteria) this;
        }

        public Criteria andProjectIdIsNotNull() {
            addCriterion("project_id is not null");
            return (Criteria) this;
        }

        public Criteria andProjectIdEqualTo(Integer value) {
            addCriterion("project_id =", value, "projectId");
            return (Criteria) this;
        }

        public Criteria andProjectIdNotEqualTo(Integer value) {
            addCriterion("project_id <>", value, "projectId");
            return (Criteria) this;
        }

        public Criteria andProjectIdGreaterThan(Integer value) {
            addCriterion("project_id >", value, "projectId");
            return (Criteria) this;
        }

        public Criteria andProjectIdGreaterThanOrEqualTo(Integer value) {
            addCriterion("project_id >=", value, "projectId");
            return (Criteria) this;
        }

        public Criteria andProjectIdLessThan(Integer value) {
            addCriterion("project_id <", value, "projectId");
            return (Criteria) this;
        }

        public Criteria andProjectIdLessThanOrEqualTo(Integer value) {
            addCriterion("project_id <=", value, "projectId");
            return (Criteria) this;
        }

        public Criteria andProjectIdIn(List<Integer> values) {
            addCriterion("project_id in", values, "projectId");
            return (Criteria) this;
        }

        public Criteria andProjectIdNotIn(List<Integer> values) {
            addCriterion("project_id not in", values, "projectId");
            return (Criteria) this;
        }

        public Criteria andProjectIdBetween(Integer value1, Integer value2) {
            addCriterion("project_id between", value1, value2, "projectId");
            return (Criteria) this;
        }

        public Criteria andProjectIdNotBetween(Integer value1, Integer value2) {
            addCriterion("project_id not between", value1, value2, "projectId");
            return (Criteria) this;
        }

        public Criteria andCategoryClsIsNull() {
            addCriterion("category_cls is null");
            return (Criteria) this;
        }

        public Criteria andCategoryClsIsNotNull() {
            addCriterion("category_cls is not null");
            return (Criteria) this;
        }

        public Criteria andCategoryClsEqualTo(Integer value) {
            addCriterion("category_cls =", value, "categoryCls");
            return (Criteria) this;
        }

        public Criteria andCategoryClsNotEqualTo(Integer value) {
            addCriterion("category_cls <>", value, "categoryCls");
            return (Criteria) this;
        }

        public Criteria andCategoryClsGreaterThan(Integer value) {
            addCriterion("category_cls >", value, "categoryCls");
            return (Criteria) this;
        }

        public Criteria andCategoryClsGreaterThanOrEqualTo(Integer value) {
            addCriterion("category_cls >=", value, "categoryCls");
            return (Criteria) this;
        }

        public Criteria andCategoryClsLessThan(Integer value) {
            addCriterion("category_cls <", value, "categoryCls");
            return (Criteria) this;
        }

        public Criteria andCategoryClsLessThanOrEqualTo(Integer value) {
            addCriterion("category_cls <=", value, "categoryCls");
            return (Criteria) this;
        }

        public Criteria andCategoryClsIn(List<Integer> values) {
            addCriterion("category_cls in", values, "categoryCls");
            return (Criteria) this;
        }

        public Criteria andCategoryClsNotIn(List<Integer> values) {
            addCriterion("category_cls not in", values, "categoryCls");
            return (Criteria) this;
        }

        public Criteria andCategoryClsBetween(Integer value1, Integer value2) {
            addCriterion("category_cls between", value1, value2, "categoryCls");
            return (Criteria) this;
        }

        public Criteria andCategoryClsNotBetween(Integer value1, Integer value2) {
            addCriterion("category_cls not between", value1, value2, "categoryCls");
            return (Criteria) this;
        }

        public Criteria andAreaIdIsNull() {
            addCriterion("area_id is null");
            return (Criteria) this;
        }

        public Criteria andAreaIdIsNotNull() {
            addCriterion("area_id is not null");
            return (Criteria) this;
        }

        public Criteria andAreaIdEqualTo(Integer value) {
            addCriterion("area_id =", value, "areaId");
            return (Criteria) this;
        }

        public Criteria andAreaIdNotEqualTo(Integer value) {
            addCriterion("area_id <>", value, "areaId");
            return (Criteria) this;
        }

        public Criteria andAreaIdGreaterThan(Integer value) {
            addCriterion("area_id >", value, "areaId");
            return (Criteria) this;
        }

        public Criteria andAreaIdGreaterThanOrEqualTo(Integer value) {
            addCriterion("area_id >=", value, "areaId");
            return (Criteria) this;
        }

        public Criteria andAreaIdLessThan(Integer value) {
            addCriterion("area_id <", value, "areaId");
            return (Criteria) this;
        }

        public Criteria andAreaIdLessThanOrEqualTo(Integer value) {
            addCriterion("area_id <=", value, "areaId");
            return (Criteria) this;
        }

        public Criteria andAreaIdIn(List<Integer> values) {
            addCriterion("area_id in", values, "areaId");
            return (Criteria) this;
        }

        public Criteria andAreaIdNotIn(List<Integer> values) {
            addCriterion("area_id not in", values, "areaId");
            return (Criteria) this;
        }

        public Criteria andAreaIdBetween(Integer value1, Integer value2) {
            addCriterion("area_id between", value1, value2, "areaId");
            return (Criteria) this;
        }

        public Criteria andAreaIdNotBetween(Integer value1, Integer value2) {
            addCriterion("area_id not between", value1, value2, "areaId");
            return (Criteria) this;
        }

        public Criteria andAreaFatherIdIsNull() {
            addCriterion("area_father_id is null");
            return (Criteria) this;
        }

        public Criteria andAreaFatherIdIsNotNull() {
            addCriterion("area_father_id is not null");
            return (Criteria) this;
        }

        public Criteria andAreaFatherIdEqualTo(Integer value) {
            addCriterion("area_father_id =", value, "areaFatherId");
            return (Criteria) this;
        }

        public Criteria andAreaFatherIdNotEqualTo(Integer value) {
            addCriterion("area_father_id <>", value, "areaFatherId");
            return (Criteria) this;
        }

        public Criteria andAreaFatherIdGreaterThan(Integer value) {
            addCriterion("area_father_id >", value, "areaFatherId");
            return (Criteria) this;
        }

        public Criteria andAreaFatherIdGreaterThanOrEqualTo(Integer value) {
            addCriterion("area_father_id >=", value, "areaFatherId");
            return (Criteria) this;
        }

        public Criteria andAreaFatherIdLessThan(Integer value) {
            addCriterion("area_father_id <", value, "areaFatherId");
            return (Criteria) this;
        }

        public Criteria andAreaFatherIdLessThanOrEqualTo(Integer value) {
            addCriterion("area_father_id <=", value, "areaFatherId");
            return (Criteria) this;
        }

        public Criteria andAreaFatherIdIn(List<Integer> values) {
            addCriterion("area_father_id in", values, "areaFatherId");
            return (Criteria) this;
        }

        public Criteria andAreaFatherIdNotIn(List<Integer> values) {
            addCriterion("area_father_id not in", values, "areaFatherId");
            return (Criteria) this;
        }

        public Criteria andAreaFatherIdBetween(Integer value1, Integer value2) {
            addCriterion("area_father_id between", value1, value2, "areaFatherId");
            return (Criteria) this;
        }

        public Criteria andAreaFatherIdNotBetween(Integer value1, Integer value2) {
            addCriterion("area_father_id not between", value1, value2, "areaFatherId");
            return (Criteria) this;
        }

        public Criteria andAreaNameIsNull() {
            addCriterion("area_name is null");
            return (Criteria) this;
        }

        public Criteria andAreaNameIsNotNull() {
            addCriterion("area_name is not null");
            return (Criteria) this;
        }

        public Criteria andAreaNameEqualTo(String value) {
            addCriterion("area_name =", value, "areaName");
            return (Criteria) this;
        }

        public Criteria andAreaNameNotEqualTo(String value) {
            addCriterion("area_name <>", value, "areaName");
            return (Criteria) this;
        }

        public Criteria andAreaNameGreaterThan(String value) {
            addCriterion("area_name >", value, "areaName");
            return (Criteria) this;
        }

        public Criteria andAreaNameGreaterThanOrEqualTo(String value) {
            addCriterion("area_name >=", value, "areaName");
            return (Criteria) this;
        }

        public Criteria andAreaNameLessThan(String value) {
            addCriterion("area_name <", value, "areaName");
            return (Criteria) this;
        }

        public Criteria andAreaNameLessThanOrEqualTo(String value) {
            addCriterion("area_name <=", value, "areaName");
            return (Criteria) this;
        }

        public Criteria andAreaNameLike(String value) {
            addCriterion("area_name like", value, "areaName");
            return (Criteria) this;
        }

        public Criteria andAreaNameNotLike(String value) {
            addCriterion("area_name not like", value, "areaName");
            return (Criteria) this;
        }

        public Criteria andAreaNameIn(List<String> values) {
            addCriterion("area_name in", values, "areaName");
            return (Criteria) this;
        }

        public Criteria andAreaNameNotIn(List<String> values) {
            addCriterion("area_name not in", values, "areaName");
            return (Criteria) this;
        }

        public Criteria andAreaNameBetween(String value1, String value2) {
            addCriterion("area_name between", value1, value2, "areaName");
            return (Criteria) this;
        }

        public Criteria andAreaNameNotBetween(String value1, String value2) {
            addCriterion("area_name not between", value1, value2, "areaName");
            return (Criteria) this;
        }

        public Criteria andAreaPathAndIdIsNull() {
            addCriterion("area_path_and_id is null");
            return (Criteria) this;
        }

        public Criteria andAreaPathAndIdIsNotNull() {
            addCriterion("area_path_and_id is not null");
            return (Criteria) this;
        }

        public Criteria andAreaPathAndIdEqualTo(String value) {
            addCriterion("area_path_and_id =", value, "areaPathAndId");
            return (Criteria) this;
        }

        public Criteria andAreaPathAndIdNotEqualTo(String value) {
            addCriterion("area_path_and_id <>", value, "areaPathAndId");
            return (Criteria) this;
        }

        public Criteria andAreaPathAndIdGreaterThan(String value) {
            addCriterion("area_path_and_id >", value, "areaPathAndId");
            return (Criteria) this;
        }

        public Criteria andAreaPathAndIdGreaterThanOrEqualTo(String value) {
            addCriterion("area_path_and_id >=", value, "areaPathAndId");
            return (Criteria) this;
        }

        public Criteria andAreaPathAndIdLessThan(String value) {
            addCriterion("area_path_and_id <", value, "areaPathAndId");
            return (Criteria) this;
        }

        public Criteria andAreaPathAndIdLessThanOrEqualTo(String value) {
            addCriterion("area_path_and_id <=", value, "areaPathAndId");
            return (Criteria) this;
        }

        public Criteria andAreaPathAndIdLike(String value) {
            addCriterion("area_path_and_id like", value, "areaPathAndId");
            return (Criteria) this;
        }

        public Criteria andAreaPathAndIdNotLike(String value) {
            addCriterion("area_path_and_id not like", value, "areaPathAndId");
            return (Criteria) this;
        }

        public Criteria andAreaPathAndIdIn(List<String> values) {
            addCriterion("area_path_and_id in", values, "areaPathAndId");
            return (Criteria) this;
        }

        public Criteria andAreaPathAndIdNotIn(List<String> values) {
            addCriterion("area_path_and_id not in", values, "areaPathAndId");
            return (Criteria) this;
        }

        public Criteria andAreaPathAndIdBetween(String value1, String value2) {
            addCriterion("area_path_and_id between", value1, value2, "areaPathAndId");
            return (Criteria) this;
        }

        public Criteria andAreaPathAndIdNotBetween(String value1, String value2) {
            addCriterion("area_path_and_id not between", value1, value2, "areaPathAndId");
            return (Criteria) this;
        }

        public Criteria andCategoryKeyIsNull() {
            addCriterion("category_key is null");
            return (Criteria) this;
        }

        public Criteria andCategoryKeyIsNotNull() {
            addCriterion("category_key is not null");
            return (Criteria) this;
        }

        public Criteria andCategoryKeyEqualTo(String value) {
            addCriterion("category_key =", value, "categoryKey");
            return (Criteria) this;
        }

        public Criteria andCategoryKeyNotEqualTo(String value) {
            addCriterion("category_key <>", value, "categoryKey");
            return (Criteria) this;
        }

        public Criteria andCategoryKeyGreaterThan(String value) {
            addCriterion("category_key >", value, "categoryKey");
            return (Criteria) this;
        }

        public Criteria andCategoryKeyGreaterThanOrEqualTo(String value) {
            addCriterion("category_key >=", value, "categoryKey");
            return (Criteria) this;
        }

        public Criteria andCategoryKeyLessThan(String value) {
            addCriterion("category_key <", value, "categoryKey");
            return (Criteria) this;
        }

        public Criteria andCategoryKeyLessThanOrEqualTo(String value) {
            addCriterion("category_key <=", value, "categoryKey");
            return (Criteria) this;
        }

        public Criteria andCategoryKeyLike(String value) {
            addCriterion("category_key like", value, "categoryKey");
            return (Criteria) this;
        }

        public Criteria andCategoryKeyNotLike(String value) {
            addCriterion("category_key not like", value, "categoryKey");
            return (Criteria) this;
        }

        public Criteria andCategoryKeyIn(List<String> values) {
            addCriterion("category_key in", values, "categoryKey");
            return (Criteria) this;
        }

        public Criteria andCategoryKeyNotIn(List<String> values) {
            addCriterion("category_key not in", values, "categoryKey");
            return (Criteria) this;
        }

        public Criteria andCategoryKeyBetween(String value1, String value2) {
            addCriterion("category_key between", value1, value2, "categoryKey");
            return (Criteria) this;
        }

        public Criteria andCategoryKeyNotBetween(String value1, String value2) {
            addCriterion("category_key not between", value1, value2, "categoryKey");
            return (Criteria) this;
        }

        public Criteria andCategoryFatherKeyIsNull() {
            addCriterion("category_father_key is null");
            return (Criteria) this;
        }

        public Criteria andCategoryFatherKeyIsNotNull() {
            addCriterion("category_father_key is not null");
            return (Criteria) this;
        }

        public Criteria andCategoryFatherKeyEqualTo(String value) {
            addCriterion("category_father_key =", value, "categoryFatherKey");
            return (Criteria) this;
        }

        public Criteria andCategoryFatherKeyNotEqualTo(String value) {
            addCriterion("category_father_key <>", value, "categoryFatherKey");
            return (Criteria) this;
        }

        public Criteria andCategoryFatherKeyGreaterThan(String value) {
            addCriterion("category_father_key >", value, "categoryFatherKey");
            return (Criteria) this;
        }

        public Criteria andCategoryFatherKeyGreaterThanOrEqualTo(String value) {
            addCriterion("category_father_key >=", value, "categoryFatherKey");
            return (Criteria) this;
        }

        public Criteria andCategoryFatherKeyLessThan(String value) {
            addCriterion("category_father_key <", value, "categoryFatherKey");
            return (Criteria) this;
        }

        public Criteria andCategoryFatherKeyLessThanOrEqualTo(String value) {
            addCriterion("category_father_key <=", value, "categoryFatherKey");
            return (Criteria) this;
        }

        public Criteria andCategoryFatherKeyLike(String value) {
            addCriterion("category_father_key like", value, "categoryFatherKey");
            return (Criteria) this;
        }

        public Criteria andCategoryFatherKeyNotLike(String value) {
            addCriterion("category_father_key not like", value, "categoryFatherKey");
            return (Criteria) this;
        }

        public Criteria andCategoryFatherKeyIn(List<String> values) {
            addCriterion("category_father_key in", values, "categoryFatherKey");
            return (Criteria) this;
        }

        public Criteria andCategoryFatherKeyNotIn(List<String> values) {
            addCriterion("category_father_key not in", values, "categoryFatherKey");
            return (Criteria) this;
        }

        public Criteria andCategoryFatherKeyBetween(String value1, String value2) {
            addCriterion("category_father_key between", value1, value2, "categoryFatherKey");
            return (Criteria) this;
        }

        public Criteria andCategoryFatherKeyNotBetween(String value1, String value2) {
            addCriterion("category_father_key not between", value1, value2, "categoryFatherKey");
            return (Criteria) this;
        }

        public Criteria andCategoryNameIsNull() {
            addCriterion("category_name is null");
            return (Criteria) this;
        }

        public Criteria andCategoryNameIsNotNull() {
            addCriterion("category_name is not null");
            return (Criteria) this;
        }

        public Criteria andCategoryNameEqualTo(String value) {
            addCriterion("category_name =", value, "categoryName");
            return (Criteria) this;
        }

        public Criteria andCategoryNameNotEqualTo(String value) {
            addCriterion("category_name <>", value, "categoryName");
            return (Criteria) this;
        }

        public Criteria andCategoryNameGreaterThan(String value) {
            addCriterion("category_name >", value, "categoryName");
            return (Criteria) this;
        }

        public Criteria andCategoryNameGreaterThanOrEqualTo(String value) {
            addCriterion("category_name >=", value, "categoryName");
            return (Criteria) this;
        }

        public Criteria andCategoryNameLessThan(String value) {
            addCriterion("category_name <", value, "categoryName");
            return (Criteria) this;
        }

        public Criteria andCategoryNameLessThanOrEqualTo(String value) {
            addCriterion("category_name <=", value, "categoryName");
            return (Criteria) this;
        }

        public Criteria andCategoryNameLike(String value) {
            addCriterion("category_name like", value, "categoryName");
            return (Criteria) this;
        }

        public Criteria andCategoryNameNotLike(String value) {
            addCriterion("category_name not like", value, "categoryName");
            return (Criteria) this;
        }

        public Criteria andCategoryNameIn(List<String> values) {
            addCriterion("category_name in", values, "categoryName");
            return (Criteria) this;
        }

        public Criteria andCategoryNameNotIn(List<String> values) {
            addCriterion("category_name not in", values, "categoryName");
            return (Criteria) this;
        }

        public Criteria andCategoryNameBetween(String value1, String value2) {
            addCriterion("category_name between", value1, value2, "categoryName");
            return (Criteria) this;
        }

        public Criteria andCategoryNameNotBetween(String value1, String value2) {
            addCriterion("category_name not between", value1, value2, "categoryName");
            return (Criteria) this;
        }

        public Criteria andCategoryPathAndKeyIsNull() {
            addCriterion("category_path_and_key is null");
            return (Criteria) this;
        }

        public Criteria andCategoryPathAndKeyIsNotNull() {
            addCriterion("category_path_and_key is not null");
            return (Criteria) this;
        }

        public Criteria andCategoryPathAndKeyEqualTo(String value) {
            addCriterion("category_path_and_key =", value, "categoryPathAndKey");
            return (Criteria) this;
        }

        public Criteria andCategoryPathAndKeyNotEqualTo(String value) {
            addCriterion("category_path_and_key <>", value, "categoryPathAndKey");
            return (Criteria) this;
        }

        public Criteria andCategoryPathAndKeyGreaterThan(String value) {
            addCriterion("category_path_and_key >", value, "categoryPathAndKey");
            return (Criteria) this;
        }

        public Criteria andCategoryPathAndKeyGreaterThanOrEqualTo(String value) {
            addCriterion("category_path_and_key >=", value, "categoryPathAndKey");
            return (Criteria) this;
        }

        public Criteria andCategoryPathAndKeyLessThan(String value) {
            addCriterion("category_path_and_key <", value, "categoryPathAndKey");
            return (Criteria) this;
        }

        public Criteria andCategoryPathAndKeyLessThanOrEqualTo(String value) {
            addCriterion("category_path_and_key <=", value, "categoryPathAndKey");
            return (Criteria) this;
        }

        public Criteria andCategoryPathAndKeyLike(String value) {
            addCriterion("category_path_and_key like", value, "categoryPathAndKey");
            return (Criteria) this;
        }

        public Criteria andCategoryPathAndKeyNotLike(String value) {
            addCriterion("category_path_and_key not like", value, "categoryPathAndKey");
            return (Criteria) this;
        }

        public Criteria andCategoryPathAndKeyIn(List<String> values) {
            addCriterion("category_path_and_key in", values, "categoryPathAndKey");
            return (Criteria) this;
        }

        public Criteria andCategoryPathAndKeyNotIn(List<String> values) {
            addCriterion("category_path_and_key not in", values, "categoryPathAndKey");
            return (Criteria) this;
        }

        public Criteria andCategoryPathAndKeyBetween(String value1, String value2) {
            addCriterion("category_path_and_key between", value1, value2, "categoryPathAndKey");
            return (Criteria) this;
        }

        public Criteria andCategoryPathAndKeyNotBetween(String value1, String value2) {
            addCriterion("category_path_and_key not between", value1, value2, "categoryPathAndKey");
            return (Criteria) this;
        }

        public Criteria andHasSubIsNull() {
            addCriterion("has_sub is null");
            return (Criteria) this;
        }

        public Criteria andHasSubIsNotNull() {
            addCriterion("has_sub is not null");
            return (Criteria) this;
        }

        public Criteria andHasSubEqualTo(Integer value) {
            addCriterion("has_sub =", value, "hasSub");
            return (Criteria) this;
        }

        public Criteria andHasSubNotEqualTo(Integer value) {
            addCriterion("has_sub <>", value, "hasSub");
            return (Criteria) this;
        }

        public Criteria andHasSubGreaterThan(Integer value) {
            addCriterion("has_sub >", value, "hasSub");
            return (Criteria) this;
        }

        public Criteria andHasSubGreaterThanOrEqualTo(Integer value) {
            addCriterion("has_sub >=", value, "hasSub");
            return (Criteria) this;
        }

        public Criteria andHasSubLessThan(Integer value) {
            addCriterion("has_sub <", value, "hasSub");
            return (Criteria) this;
        }

        public Criteria andHasSubLessThanOrEqualTo(Integer value) {
            addCriterion("has_sub <=", value, "hasSub");
            return (Criteria) this;
        }

        public Criteria andHasSubIn(List<Integer> values) {
            addCriterion("has_sub in", values, "hasSub");
            return (Criteria) this;
        }

        public Criteria andHasSubNotIn(List<Integer> values) {
            addCriterion("has_sub not in", values, "hasSub");
            return (Criteria) this;
        }

        public Criteria andHasSubBetween(Integer value1, Integer value2) {
            addCriterion("has_sub between", value1, value2, "hasSub");
            return (Criteria) this;
        }

        public Criteria andHasSubNotBetween(Integer value1, Integer value2) {
            addCriterion("has_sub not between", value1, value2, "hasSub");
            return (Criteria) this;
        }

        public Criteria andRootCategoryIdIsNull() {
            addCriterion("root_category_id is null");
            return (Criteria) this;
        }

        public Criteria andRootCategoryIdIsNotNull() {
            addCriterion("root_category_id is not null");
            return (Criteria) this;
        }

        public Criteria andRootCategoryIdEqualTo(Integer value) {
            addCriterion("root_category_id =", value, "rootCategoryId");
            return (Criteria) this;
        }

        public Criteria andRootCategoryIdNotEqualTo(Integer value) {
            addCriterion("root_category_id <>", value, "rootCategoryId");
            return (Criteria) this;
        }

        public Criteria andRootCategoryIdGreaterThan(Integer value) {
            addCriterion("root_category_id >", value, "rootCategoryId");
            return (Criteria) this;
        }

        public Criteria andRootCategoryIdGreaterThanOrEqualTo(Integer value) {
            addCriterion("root_category_id >=", value, "rootCategoryId");
            return (Criteria) this;
        }

        public Criteria andRootCategoryIdLessThan(Integer value) {
            addCriterion("root_category_id <", value, "rootCategoryId");
            return (Criteria) this;
        }

        public Criteria andRootCategoryIdLessThanOrEqualTo(Integer value) {
            addCriterion("root_category_id <=", value, "rootCategoryId");
            return (Criteria) this;
        }

        public Criteria andRootCategoryIdIn(List<Integer> values) {
            addCriterion("root_category_id in", values, "rootCategoryId");
            return (Criteria) this;
        }

        public Criteria andRootCategoryIdNotIn(List<Integer> values) {
            addCriterion("root_category_id not in", values, "rootCategoryId");
            return (Criteria) this;
        }

        public Criteria andRootCategoryIdBetween(Integer value1, Integer value2) {
            addCriterion("root_category_id between", value1, value2, "rootCategoryId");
            return (Criteria) this;
        }

        public Criteria andRootCategoryIdNotBetween(Integer value1, Integer value2) {
            addCriterion("root_category_id not between", value1, value2, "rootCategoryId");
            return (Criteria) this;
        }

        public Criteria andCategoryOrderIsNull() {
            addCriterion("category_order is null");
            return (Criteria) this;
        }

        public Criteria andCategoryOrderIsNotNull() {
            addCriterion("category_order is not null");
            return (Criteria) this;
        }

        public Criteria andCategoryOrderEqualTo(String value) {
            addCriterion("category_order =", value, "categoryOrder");
            return (Criteria) this;
        }

        public Criteria andCategoryOrderNotEqualTo(String value) {
            addCriterion("category_order <>", value, "categoryOrder");
            return (Criteria) this;
        }

        public Criteria andCategoryOrderGreaterThan(String value) {
            addCriterion("category_order >", value, "categoryOrder");
            return (Criteria) this;
        }

        public Criteria andCategoryOrderGreaterThanOrEqualTo(String value) {
            addCriterion("category_order >=", value, "categoryOrder");
            return (Criteria) this;
        }

        public Criteria andCategoryOrderLessThan(String value) {
            addCriterion("category_order <", value, "categoryOrder");
            return (Criteria) this;
        }

        public Criteria andCategoryOrderLessThanOrEqualTo(String value) {
            addCriterion("category_order <=", value, "categoryOrder");
            return (Criteria) this;
        }

        public Criteria andCategoryOrderLike(String value) {
            addCriterion("category_order like", value, "categoryOrder");
            return (Criteria) this;
        }

        public Criteria andCategoryOrderNotLike(String value) {
            addCriterion("category_order not like", value, "categoryOrder");
            return (Criteria) this;
        }

        public Criteria andCategoryOrderIn(List<String> values) {
            addCriterion("category_order in", values, "categoryOrder");
            return (Criteria) this;
        }

        public Criteria andCategoryOrderNotIn(List<String> values) {
            addCriterion("category_order not in", values, "categoryOrder");
            return (Criteria) this;
        }

        public Criteria andCategoryOrderBetween(String value1, String value2) {
            addCriterion("category_order between", value1, value2, "categoryOrder");
            return (Criteria) this;
        }

        public Criteria andCategoryOrderNotBetween(String value1, String value2) {
            addCriterion("category_order not between", value1, value2, "categoryOrder");
            return (Criteria) this;
        }

        public Criteria andIssueCountIsNull() {
            addCriterion("issue_count is null");
            return (Criteria) this;
        }

        public Criteria andIssueCountIsNotNull() {
            addCriterion("issue_count is not null");
            return (Criteria) this;
        }

        public Criteria andIssueCountEqualTo(Integer value) {
            addCriterion("issue_count =", value, "issueCount");
            return (Criteria) this;
        }

        public Criteria andIssueCountNotEqualTo(Integer value) {
            addCriterion("issue_count <>", value, "issueCount");
            return (Criteria) this;
        }

        public Criteria andIssueCountGreaterThan(Integer value) {
            addCriterion("issue_count >", value, "issueCount");
            return (Criteria) this;
        }

        public Criteria andIssueCountGreaterThanOrEqualTo(Integer value) {
            addCriterion("issue_count >=", value, "issueCount");
            return (Criteria) this;
        }

        public Criteria andIssueCountLessThan(Integer value) {
            addCriterion("issue_count <", value, "issueCount");
            return (Criteria) this;
        }

        public Criteria andIssueCountLessThanOrEqualTo(Integer value) {
            addCriterion("issue_count <=", value, "issueCount");
            return (Criteria) this;
        }

        public Criteria andIssueCountIn(List<Integer> values) {
            addCriterion("issue_count in", values, "issueCount");
            return (Criteria) this;
        }

        public Criteria andIssueCountNotIn(List<Integer> values) {
            addCriterion("issue_count not in", values, "issueCount");
            return (Criteria) this;
        }

        public Criteria andIssueCountBetween(Integer value1, Integer value2) {
            addCriterion("issue_count between", value1, value2, "issueCount");
            return (Criteria) this;
        }

        public Criteria andIssueCountNotBetween(Integer value1, Integer value2) {
            addCriterion("issue_count not between", value1, value2, "issueCount");
            return (Criteria) this;
        }

        public Criteria andRecordCountIsNull() {
            addCriterion("record_count is null");
            return (Criteria) this;
        }

        public Criteria andRecordCountIsNotNull() {
            addCriterion("record_count is not null");
            return (Criteria) this;
        }

        public Criteria andRecordCountEqualTo(Integer value) {
            addCriterion("record_count =", value, "recordCount");
            return (Criteria) this;
        }

        public Criteria andRecordCountNotEqualTo(Integer value) {
            addCriterion("record_count <>", value, "recordCount");
            return (Criteria) this;
        }

        public Criteria andRecordCountGreaterThan(Integer value) {
            addCriterion("record_count >", value, "recordCount");
            return (Criteria) this;
        }

        public Criteria andRecordCountGreaterThanOrEqualTo(Integer value) {
            addCriterion("record_count >=", value, "recordCount");
            return (Criteria) this;
        }

        public Criteria andRecordCountLessThan(Integer value) {
            addCriterion("record_count <", value, "recordCount");
            return (Criteria) this;
        }

        public Criteria andRecordCountLessThanOrEqualTo(Integer value) {
            addCriterion("record_count <=", value, "recordCount");
            return (Criteria) this;
        }

        public Criteria andRecordCountIn(List<Integer> values) {
            addCriterion("record_count in", values, "recordCount");
            return (Criteria) this;
        }

        public Criteria andRecordCountNotIn(List<Integer> values) {
            addCriterion("record_count not in", values, "recordCount");
            return (Criteria) this;
        }

        public Criteria andRecordCountBetween(Integer value1, Integer value2) {
            addCriterion("record_count between", value1, value2, "recordCount");
            return (Criteria) this;
        }

        public Criteria andRecordCountNotBetween(Integer value1, Integer value2) {
            addCriterion("record_count not between", value1, value2, "recordCount");
            return (Criteria) this;
        }

        public Criteria andIssueNewCountIsNull() {
            addCriterion("issue_new_count is null");
            return (Criteria) this;
        }

        public Criteria andIssueNewCountIsNotNull() {
            addCriterion("issue_new_count is not null");
            return (Criteria) this;
        }

        public Criteria andIssueNewCountEqualTo(Integer value) {
            addCriterion("issue_new_count =", value, "issueNewCount");
            return (Criteria) this;
        }

        public Criteria andIssueNewCountNotEqualTo(Integer value) {
            addCriterion("issue_new_count <>", value, "issueNewCount");
            return (Criteria) this;
        }

        public Criteria andIssueNewCountGreaterThan(Integer value) {
            addCriterion("issue_new_count >", value, "issueNewCount");
            return (Criteria) this;
        }

        public Criteria andIssueNewCountGreaterThanOrEqualTo(Integer value) {
            addCriterion("issue_new_count >=", value, "issueNewCount");
            return (Criteria) this;
        }

        public Criteria andIssueNewCountLessThan(Integer value) {
            addCriterion("issue_new_count <", value, "issueNewCount");
            return (Criteria) this;
        }

        public Criteria andIssueNewCountLessThanOrEqualTo(Integer value) {
            addCriterion("issue_new_count <=", value, "issueNewCount");
            return (Criteria) this;
        }

        public Criteria andIssueNewCountIn(List<Integer> values) {
            addCriterion("issue_new_count in", values, "issueNewCount");
            return (Criteria) this;
        }

        public Criteria andIssueNewCountNotIn(List<Integer> values) {
            addCriterion("issue_new_count not in", values, "issueNewCount");
            return (Criteria) this;
        }

        public Criteria andIssueNewCountBetween(Integer value1, Integer value2) {
            addCriterion("issue_new_count between", value1, value2, "issueNewCount");
            return (Criteria) this;
        }

        public Criteria andIssueNewCountNotBetween(Integer value1, Integer value2) {
            addCriterion("issue_new_count not between", value1, value2, "issueNewCount");
            return (Criteria) this;
        }

        public Criteria andRecordNewCountIsNull() {
            addCriterion("record_new_count is null");
            return (Criteria) this;
        }

        public Criteria andRecordNewCountIsNotNull() {
            addCriterion("record_new_count is not null");
            return (Criteria) this;
        }

        public Criteria andRecordNewCountEqualTo(Integer value) {
            addCriterion("record_new_count =", value, "recordNewCount");
            return (Criteria) this;
        }

        public Criteria andRecordNewCountNotEqualTo(Integer value) {
            addCriterion("record_new_count <>", value, "recordNewCount");
            return (Criteria) this;
        }

        public Criteria andRecordNewCountGreaterThan(Integer value) {
            addCriterion("record_new_count >", value, "recordNewCount");
            return (Criteria) this;
        }

        public Criteria andRecordNewCountGreaterThanOrEqualTo(Integer value) {
            addCriterion("record_new_count >=", value, "recordNewCount");
            return (Criteria) this;
        }

        public Criteria andRecordNewCountLessThan(Integer value) {
            addCriterion("record_new_count <", value, "recordNewCount");
            return (Criteria) this;
        }

        public Criteria andRecordNewCountLessThanOrEqualTo(Integer value) {
            addCriterion("record_new_count <=", value, "recordNewCount");
            return (Criteria) this;
        }

        public Criteria andRecordNewCountIn(List<Integer> values) {
            addCriterion("record_new_count in", values, "recordNewCount");
            return (Criteria) this;
        }

        public Criteria andRecordNewCountNotIn(List<Integer> values) {
            addCriterion("record_new_count not in", values, "recordNewCount");
            return (Criteria) this;
        }

        public Criteria andRecordNewCountBetween(Integer value1, Integer value2) {
            addCriterion("record_new_count between", value1, value2, "recordNewCount");
            return (Criteria) this;
        }

        public Criteria andRecordNewCountNotBetween(Integer value1, Integer value2) {
            addCriterion("record_new_count not between", value1, value2, "recordNewCount");
            return (Criteria) this;
        }

        public Criteria andIssueNoteNoAssignCountIsNull() {
            addCriterion("issue_note_no_assign_count is null");
            return (Criteria) this;
        }

        public Criteria andIssueNoteNoAssignCountIsNotNull() {
            addCriterion("issue_note_no_assign_count is not null");
            return (Criteria) this;
        }

        public Criteria andIssueNoteNoAssignCountEqualTo(Integer value) {
            addCriterion("issue_note_no_assign_count =", value, "issueNoteNoAssignCount");
            return (Criteria) this;
        }

        public Criteria andIssueNoteNoAssignCountNotEqualTo(Integer value) {
            addCriterion("issue_note_no_assign_count <>", value, "issueNoteNoAssignCount");
            return (Criteria) this;
        }

        public Criteria andIssueNoteNoAssignCountGreaterThan(Integer value) {
            addCriterion("issue_note_no_assign_count >", value, "issueNoteNoAssignCount");
            return (Criteria) this;
        }

        public Criteria andIssueNoteNoAssignCountGreaterThanOrEqualTo(Integer value) {
            addCriterion("issue_note_no_assign_count >=", value, "issueNoteNoAssignCount");
            return (Criteria) this;
        }

        public Criteria andIssueNoteNoAssignCountLessThan(Integer value) {
            addCriterion("issue_note_no_assign_count <", value, "issueNoteNoAssignCount");
            return (Criteria) this;
        }

        public Criteria andIssueNoteNoAssignCountLessThanOrEqualTo(Integer value) {
            addCriterion("issue_note_no_assign_count <=", value, "issueNoteNoAssignCount");
            return (Criteria) this;
        }

        public Criteria andIssueNoteNoAssignCountIn(List<Integer> values) {
            addCriterion("issue_note_no_assign_count in", values, "issueNoteNoAssignCount");
            return (Criteria) this;
        }

        public Criteria andIssueNoteNoAssignCountNotIn(List<Integer> values) {
            addCriterion("issue_note_no_assign_count not in", values, "issueNoteNoAssignCount");
            return (Criteria) this;
        }

        public Criteria andIssueNoteNoAssignCountBetween(Integer value1, Integer value2) {
            addCriterion("issue_note_no_assign_count between", value1, value2, "issueNoteNoAssignCount");
            return (Criteria) this;
        }

        public Criteria andIssueNoteNoAssignCountNotBetween(Integer value1, Integer value2) {
            addCriterion("issue_note_no_assign_count not between", value1, value2, "issueNoteNoAssignCount");
            return (Criteria) this;
        }

        public Criteria andIssueAssignNoReformCountIsNull() {
            addCriterion("issue_assign_no_reform_count is null");
            return (Criteria) this;
        }

        public Criteria andIssueAssignNoReformCountIsNotNull() {
            addCriterion("issue_assign_no_reform_count is not null");
            return (Criteria) this;
        }

        public Criteria andIssueAssignNoReformCountEqualTo(Integer value) {
            addCriterion("issue_assign_no_reform_count =", value, "issueAssignNoReformCount");
            return (Criteria) this;
        }

        public Criteria andIssueAssignNoReformCountNotEqualTo(Integer value) {
            addCriterion("issue_assign_no_reform_count <>", value, "issueAssignNoReformCount");
            return (Criteria) this;
        }

        public Criteria andIssueAssignNoReformCountGreaterThan(Integer value) {
            addCriterion("issue_assign_no_reform_count >", value, "issueAssignNoReformCount");
            return (Criteria) this;
        }

        public Criteria andIssueAssignNoReformCountGreaterThanOrEqualTo(Integer value) {
            addCriterion("issue_assign_no_reform_count >=", value, "issueAssignNoReformCount");
            return (Criteria) this;
        }

        public Criteria andIssueAssignNoReformCountLessThan(Integer value) {
            addCriterion("issue_assign_no_reform_count <", value, "issueAssignNoReformCount");
            return (Criteria) this;
        }

        public Criteria andIssueAssignNoReformCountLessThanOrEqualTo(Integer value) {
            addCriterion("issue_assign_no_reform_count <=", value, "issueAssignNoReformCount");
            return (Criteria) this;
        }

        public Criteria andIssueAssignNoReformCountIn(List<Integer> values) {
            addCriterion("issue_assign_no_reform_count in", values, "issueAssignNoReformCount");
            return (Criteria) this;
        }

        public Criteria andIssueAssignNoReformCountNotIn(List<Integer> values) {
            addCriterion("issue_assign_no_reform_count not in", values, "issueAssignNoReformCount");
            return (Criteria) this;
        }

        public Criteria andIssueAssignNoReformCountBetween(Integer value1, Integer value2) {
            addCriterion("issue_assign_no_reform_count between", value1, value2, "issueAssignNoReformCount");
            return (Criteria) this;
        }

        public Criteria andIssueAssignNoReformCountNotBetween(Integer value1, Integer value2) {
            addCriterion("issue_assign_no_reform_count not between", value1, value2, "issueAssignNoReformCount");
            return (Criteria) this;
        }

        public Criteria andIssueReformNoCheckCountIsNull() {
            addCriterion("issue_reform_no_check_count is null");
            return (Criteria) this;
        }

        public Criteria andIssueReformNoCheckCountIsNotNull() {
            addCriterion("issue_reform_no_check_count is not null");
            return (Criteria) this;
        }

        public Criteria andIssueReformNoCheckCountEqualTo(Integer value) {
            addCriterion("issue_reform_no_check_count =", value, "issueReformNoCheckCount");
            return (Criteria) this;
        }

        public Criteria andIssueReformNoCheckCountNotEqualTo(Integer value) {
            addCriterion("issue_reform_no_check_count <>", value, "issueReformNoCheckCount");
            return (Criteria) this;
        }

        public Criteria andIssueReformNoCheckCountGreaterThan(Integer value) {
            addCriterion("issue_reform_no_check_count >", value, "issueReformNoCheckCount");
            return (Criteria) this;
        }

        public Criteria andIssueReformNoCheckCountGreaterThanOrEqualTo(Integer value) {
            addCriterion("issue_reform_no_check_count >=", value, "issueReformNoCheckCount");
            return (Criteria) this;
        }

        public Criteria andIssueReformNoCheckCountLessThan(Integer value) {
            addCriterion("issue_reform_no_check_count <", value, "issueReformNoCheckCount");
            return (Criteria) this;
        }

        public Criteria andIssueReformNoCheckCountLessThanOrEqualTo(Integer value) {
            addCriterion("issue_reform_no_check_count <=", value, "issueReformNoCheckCount");
            return (Criteria) this;
        }

        public Criteria andIssueReformNoCheckCountIn(List<Integer> values) {
            addCriterion("issue_reform_no_check_count in", values, "issueReformNoCheckCount");
            return (Criteria) this;
        }

        public Criteria andIssueReformNoCheckCountNotIn(List<Integer> values) {
            addCriterion("issue_reform_no_check_count not in", values, "issueReformNoCheckCount");
            return (Criteria) this;
        }

        public Criteria andIssueReformNoCheckCountBetween(Integer value1, Integer value2) {
            addCriterion("issue_reform_no_check_count between", value1, value2, "issueReformNoCheckCount");
            return (Criteria) this;
        }

        public Criteria andIssueReformNoCheckCountNotBetween(Integer value1, Integer value2) {
            addCriterion("issue_reform_no_check_count not between", value1, value2, "issueReformNoCheckCount");
            return (Criteria) this;
        }

        public Criteria andIssueCheckYesCountIsNull() {
            addCriterion("issue_check_yes_count is null");
            return (Criteria) this;
        }

        public Criteria andIssueCheckYesCountIsNotNull() {
            addCriterion("issue_check_yes_count is not null");
            return (Criteria) this;
        }

        public Criteria andIssueCheckYesCountEqualTo(Integer value) {
            addCriterion("issue_check_yes_count =", value, "issueCheckYesCount");
            return (Criteria) this;
        }

        public Criteria andIssueCheckYesCountNotEqualTo(Integer value) {
            addCriterion("issue_check_yes_count <>", value, "issueCheckYesCount");
            return (Criteria) this;
        }

        public Criteria andIssueCheckYesCountGreaterThan(Integer value) {
            addCriterion("issue_check_yes_count >", value, "issueCheckYesCount");
            return (Criteria) this;
        }

        public Criteria andIssueCheckYesCountGreaterThanOrEqualTo(Integer value) {
            addCriterion("issue_check_yes_count >=", value, "issueCheckYesCount");
            return (Criteria) this;
        }

        public Criteria andIssueCheckYesCountLessThan(Integer value) {
            addCriterion("issue_check_yes_count <", value, "issueCheckYesCount");
            return (Criteria) this;
        }

        public Criteria andIssueCheckYesCountLessThanOrEqualTo(Integer value) {
            addCriterion("issue_check_yes_count <=", value, "issueCheckYesCount");
            return (Criteria) this;
        }

        public Criteria andIssueCheckYesCountIn(List<Integer> values) {
            addCriterion("issue_check_yes_count in", values, "issueCheckYesCount");
            return (Criteria) this;
        }

        public Criteria andIssueCheckYesCountNotIn(List<Integer> values) {
            addCriterion("issue_check_yes_count not in", values, "issueCheckYesCount");
            return (Criteria) this;
        }

        public Criteria andIssueCheckYesCountBetween(Integer value1, Integer value2) {
            addCriterion("issue_check_yes_count between", value1, value2, "issueCheckYesCount");
            return (Criteria) this;
        }

        public Criteria andIssueCheckYesCountNotBetween(Integer value1, Integer value2) {
            addCriterion("issue_check_yes_count not between", value1, value2, "issueCheckYesCount");
            return (Criteria) this;
        }

        public Criteria andIssueOverdueToAssignCountIsNull() {
            addCriterion("issue_overdue_to_assign_count is null");
            return (Criteria) this;
        }

        public Criteria andIssueOverdueToAssignCountIsNotNull() {
            addCriterion("issue_overdue_to_assign_count is not null");
            return (Criteria) this;
        }

        public Criteria andIssueOverdueToAssignCountEqualTo(Integer value) {
            addCriterion("issue_overdue_to_assign_count =", value, "issueOverdueToAssignCount");
            return (Criteria) this;
        }

        public Criteria andIssueOverdueToAssignCountNotEqualTo(Integer value) {
            addCriterion("issue_overdue_to_assign_count <>", value, "issueOverdueToAssignCount");
            return (Criteria) this;
        }

        public Criteria andIssueOverdueToAssignCountGreaterThan(Integer value) {
            addCriterion("issue_overdue_to_assign_count >", value, "issueOverdueToAssignCount");
            return (Criteria) this;
        }

        public Criteria andIssueOverdueToAssignCountGreaterThanOrEqualTo(Integer value) {
            addCriterion("issue_overdue_to_assign_count >=", value, "issueOverdueToAssignCount");
            return (Criteria) this;
        }

        public Criteria andIssueOverdueToAssignCountLessThan(Integer value) {
            addCriterion("issue_overdue_to_assign_count <", value, "issueOverdueToAssignCount");
            return (Criteria) this;
        }

        public Criteria andIssueOverdueToAssignCountLessThanOrEqualTo(Integer value) {
            addCriterion("issue_overdue_to_assign_count <=", value, "issueOverdueToAssignCount");
            return (Criteria) this;
        }

        public Criteria andIssueOverdueToAssignCountIn(List<Integer> values) {
            addCriterion("issue_overdue_to_assign_count in", values, "issueOverdueToAssignCount");
            return (Criteria) this;
        }

        public Criteria andIssueOverdueToAssignCountNotIn(List<Integer> values) {
            addCriterion("issue_overdue_to_assign_count not in", values, "issueOverdueToAssignCount");
            return (Criteria) this;
        }

        public Criteria andIssueOverdueToAssignCountBetween(Integer value1, Integer value2) {
            addCriterion("issue_overdue_to_assign_count between", value1, value2, "issueOverdueToAssignCount");
            return (Criteria) this;
        }

        public Criteria andIssueOverdueToAssignCountNotBetween(Integer value1, Integer value2) {
            addCriterion("issue_overdue_to_assign_count not between", value1, value2, "issueOverdueToAssignCount");
            return (Criteria) this;
        }

        public Criteria andIssueOverdueToReformCountIsNull() {
            addCriterion("issue_overdue_to_reform_count is null");
            return (Criteria) this;
        }

        public Criteria andIssueOverdueToReformCountIsNotNull() {
            addCriterion("issue_overdue_to_reform_count is not null");
            return (Criteria) this;
        }

        public Criteria andIssueOverdueToReformCountEqualTo(Integer value) {
            addCriterion("issue_overdue_to_reform_count =", value, "issueOverdueToReformCount");
            return (Criteria) this;
        }

        public Criteria andIssueOverdueToReformCountNotEqualTo(Integer value) {
            addCriterion("issue_overdue_to_reform_count <>", value, "issueOverdueToReformCount");
            return (Criteria) this;
        }

        public Criteria andIssueOverdueToReformCountGreaterThan(Integer value) {
            addCriterion("issue_overdue_to_reform_count >", value, "issueOverdueToReformCount");
            return (Criteria) this;
        }

        public Criteria andIssueOverdueToReformCountGreaterThanOrEqualTo(Integer value) {
            addCriterion("issue_overdue_to_reform_count >=", value, "issueOverdueToReformCount");
            return (Criteria) this;
        }

        public Criteria andIssueOverdueToReformCountLessThan(Integer value) {
            addCriterion("issue_overdue_to_reform_count <", value, "issueOverdueToReformCount");
            return (Criteria) this;
        }

        public Criteria andIssueOverdueToReformCountLessThanOrEqualTo(Integer value) {
            addCriterion("issue_overdue_to_reform_count <=", value, "issueOverdueToReformCount");
            return (Criteria) this;
        }

        public Criteria andIssueOverdueToReformCountIn(List<Integer> values) {
            addCriterion("issue_overdue_to_reform_count in", values, "issueOverdueToReformCount");
            return (Criteria) this;
        }

        public Criteria andIssueOverdueToReformCountNotIn(List<Integer> values) {
            addCriterion("issue_overdue_to_reform_count not in", values, "issueOverdueToReformCount");
            return (Criteria) this;
        }

        public Criteria andIssueOverdueToReformCountBetween(Integer value1, Integer value2) {
            addCriterion("issue_overdue_to_reform_count between", value1, value2, "issueOverdueToReformCount");
            return (Criteria) this;
        }

        public Criteria andIssueOverdueToReformCountNotBetween(Integer value1, Integer value2) {
            addCriterion("issue_overdue_to_reform_count not between", value1, value2, "issueOverdueToReformCount");
            return (Criteria) this;
        }

        public Criteria andIssueOverdueToCheckCountIsNull() {
            addCriterion("issue_overdue_to_check_count is null");
            return (Criteria) this;
        }

        public Criteria andIssueOverdueToCheckCountIsNotNull() {
            addCriterion("issue_overdue_to_check_count is not null");
            return (Criteria) this;
        }

        public Criteria andIssueOverdueToCheckCountEqualTo(Integer value) {
            addCriterion("issue_overdue_to_check_count =", value, "issueOverdueToCheckCount");
            return (Criteria) this;
        }

        public Criteria andIssueOverdueToCheckCountNotEqualTo(Integer value) {
            addCriterion("issue_overdue_to_check_count <>", value, "issueOverdueToCheckCount");
            return (Criteria) this;
        }

        public Criteria andIssueOverdueToCheckCountGreaterThan(Integer value) {
            addCriterion("issue_overdue_to_check_count >", value, "issueOverdueToCheckCount");
            return (Criteria) this;
        }

        public Criteria andIssueOverdueToCheckCountGreaterThanOrEqualTo(Integer value) {
            addCriterion("issue_overdue_to_check_count >=", value, "issueOverdueToCheckCount");
            return (Criteria) this;
        }

        public Criteria andIssueOverdueToCheckCountLessThan(Integer value) {
            addCriterion("issue_overdue_to_check_count <", value, "issueOverdueToCheckCount");
            return (Criteria) this;
        }

        public Criteria andIssueOverdueToCheckCountLessThanOrEqualTo(Integer value) {
            addCriterion("issue_overdue_to_check_count <=", value, "issueOverdueToCheckCount");
            return (Criteria) this;
        }

        public Criteria andIssueOverdueToCheckCountIn(List<Integer> values) {
            addCriterion("issue_overdue_to_check_count in", values, "issueOverdueToCheckCount");
            return (Criteria) this;
        }

        public Criteria andIssueOverdueToCheckCountNotIn(List<Integer> values) {
            addCriterion("issue_overdue_to_check_count not in", values, "issueOverdueToCheckCount");
            return (Criteria) this;
        }

        public Criteria andIssueOverdueToCheckCountBetween(Integer value1, Integer value2) {
            addCriterion("issue_overdue_to_check_count between", value1, value2, "issueOverdueToCheckCount");
            return (Criteria) this;
        }

        public Criteria andIssueOverdueToCheckCountNotBetween(Integer value1, Integer value2) {
            addCriterion("issue_overdue_to_check_count not between", value1, value2, "issueOverdueToCheckCount");
            return (Criteria) this;
        }

        public Criteria andIssueOverdueCheckedCountIsNull() {
            addCriterion("issue_overdue_checked_count is null");
            return (Criteria) this;
        }

        public Criteria andIssueOverdueCheckedCountIsNotNull() {
            addCriterion("issue_overdue_checked_count is not null");
            return (Criteria) this;
        }

        public Criteria andIssueOverdueCheckedCountEqualTo(Integer value) {
            addCriterion("issue_overdue_checked_count =", value, "issueOverdueCheckedCount");
            return (Criteria) this;
        }

        public Criteria andIssueOverdueCheckedCountNotEqualTo(Integer value) {
            addCriterion("issue_overdue_checked_count <>", value, "issueOverdueCheckedCount");
            return (Criteria) this;
        }

        public Criteria andIssueOverdueCheckedCountGreaterThan(Integer value) {
            addCriterion("issue_overdue_checked_count >", value, "issueOverdueCheckedCount");
            return (Criteria) this;
        }

        public Criteria andIssueOverdueCheckedCountGreaterThanOrEqualTo(Integer value) {
            addCriterion("issue_overdue_checked_count >=", value, "issueOverdueCheckedCount");
            return (Criteria) this;
        }

        public Criteria andIssueOverdueCheckedCountLessThan(Integer value) {
            addCriterion("issue_overdue_checked_count <", value, "issueOverdueCheckedCount");
            return (Criteria) this;
        }

        public Criteria andIssueOverdueCheckedCountLessThanOrEqualTo(Integer value) {
            addCriterion("issue_overdue_checked_count <=", value, "issueOverdueCheckedCount");
            return (Criteria) this;
        }

        public Criteria andIssueOverdueCheckedCountIn(List<Integer> values) {
            addCriterion("issue_overdue_checked_count in", values, "issueOverdueCheckedCount");
            return (Criteria) this;
        }

        public Criteria andIssueOverdueCheckedCountNotIn(List<Integer> values) {
            addCriterion("issue_overdue_checked_count not in", values, "issueOverdueCheckedCount");
            return (Criteria) this;
        }

        public Criteria andIssueOverdueCheckedCountBetween(Integer value1, Integer value2) {
            addCriterion("issue_overdue_checked_count between", value1, value2, "issueOverdueCheckedCount");
            return (Criteria) this;
        }

        public Criteria andIssueOverdueCheckedCountNotBetween(Integer value1, Integer value2) {
            addCriterion("issue_overdue_checked_count not between", value1, value2, "issueOverdueCheckedCount");
            return (Criteria) this;
        }

        public Criteria andIssueIntimeToAssignCountIsNull() {
            addCriterion("issue_intime_to_assign_count is null");
            return (Criteria) this;
        }

        public Criteria andIssueIntimeToAssignCountIsNotNull() {
            addCriterion("issue_intime_to_assign_count is not null");
            return (Criteria) this;
        }

        public Criteria andIssueIntimeToAssignCountEqualTo(Integer value) {
            addCriterion("issue_intime_to_assign_count =", value, "issueIntimeToAssignCount");
            return (Criteria) this;
        }

        public Criteria andIssueIntimeToAssignCountNotEqualTo(Integer value) {
            addCriterion("issue_intime_to_assign_count <>", value, "issueIntimeToAssignCount");
            return (Criteria) this;
        }

        public Criteria andIssueIntimeToAssignCountGreaterThan(Integer value) {
            addCriterion("issue_intime_to_assign_count >", value, "issueIntimeToAssignCount");
            return (Criteria) this;
        }

        public Criteria andIssueIntimeToAssignCountGreaterThanOrEqualTo(Integer value) {
            addCriterion("issue_intime_to_assign_count >=", value, "issueIntimeToAssignCount");
            return (Criteria) this;
        }

        public Criteria andIssueIntimeToAssignCountLessThan(Integer value) {
            addCriterion("issue_intime_to_assign_count <", value, "issueIntimeToAssignCount");
            return (Criteria) this;
        }

        public Criteria andIssueIntimeToAssignCountLessThanOrEqualTo(Integer value) {
            addCriterion("issue_intime_to_assign_count <=", value, "issueIntimeToAssignCount");
            return (Criteria) this;
        }

        public Criteria andIssueIntimeToAssignCountIn(List<Integer> values) {
            addCriterion("issue_intime_to_assign_count in", values, "issueIntimeToAssignCount");
            return (Criteria) this;
        }

        public Criteria andIssueIntimeToAssignCountNotIn(List<Integer> values) {
            addCriterion("issue_intime_to_assign_count not in", values, "issueIntimeToAssignCount");
            return (Criteria) this;
        }

        public Criteria andIssueIntimeToAssignCountBetween(Integer value1, Integer value2) {
            addCriterion("issue_intime_to_assign_count between", value1, value2, "issueIntimeToAssignCount");
            return (Criteria) this;
        }

        public Criteria andIssueIntimeToAssignCountNotBetween(Integer value1, Integer value2) {
            addCriterion("issue_intime_to_assign_count not between", value1, value2, "issueIntimeToAssignCount");
            return (Criteria) this;
        }

        public Criteria andIssueIntimeToReformCountIsNull() {
            addCriterion("issue_intime_to_reform_count is null");
            return (Criteria) this;
        }

        public Criteria andIssueIntimeToReformCountIsNotNull() {
            addCriterion("issue_intime_to_reform_count is not null");
            return (Criteria) this;
        }

        public Criteria andIssueIntimeToReformCountEqualTo(Integer value) {
            addCriterion("issue_intime_to_reform_count =", value, "issueIntimeToReformCount");
            return (Criteria) this;
        }

        public Criteria andIssueIntimeToReformCountNotEqualTo(Integer value) {
            addCriterion("issue_intime_to_reform_count <>", value, "issueIntimeToReformCount");
            return (Criteria) this;
        }

        public Criteria andIssueIntimeToReformCountGreaterThan(Integer value) {
            addCriterion("issue_intime_to_reform_count >", value, "issueIntimeToReformCount");
            return (Criteria) this;
        }

        public Criteria andIssueIntimeToReformCountGreaterThanOrEqualTo(Integer value) {
            addCriterion("issue_intime_to_reform_count >=", value, "issueIntimeToReformCount");
            return (Criteria) this;
        }

        public Criteria andIssueIntimeToReformCountLessThan(Integer value) {
            addCriterion("issue_intime_to_reform_count <", value, "issueIntimeToReformCount");
            return (Criteria) this;
        }

        public Criteria andIssueIntimeToReformCountLessThanOrEqualTo(Integer value) {
            addCriterion("issue_intime_to_reform_count <=", value, "issueIntimeToReformCount");
            return (Criteria) this;
        }

        public Criteria andIssueIntimeToReformCountIn(List<Integer> values) {
            addCriterion("issue_intime_to_reform_count in", values, "issueIntimeToReformCount");
            return (Criteria) this;
        }

        public Criteria andIssueIntimeToReformCountNotIn(List<Integer> values) {
            addCriterion("issue_intime_to_reform_count not in", values, "issueIntimeToReformCount");
            return (Criteria) this;
        }

        public Criteria andIssueIntimeToReformCountBetween(Integer value1, Integer value2) {
            addCriterion("issue_intime_to_reform_count between", value1, value2, "issueIntimeToReformCount");
            return (Criteria) this;
        }

        public Criteria andIssueIntimeToReformCountNotBetween(Integer value1, Integer value2) {
            addCriterion("issue_intime_to_reform_count not between", value1, value2, "issueIntimeToReformCount");
            return (Criteria) this;
        }

        public Criteria andIssueIntimeToCheckCountIsNull() {
            addCriterion("issue_intime_to_check_count is null");
            return (Criteria) this;
        }

        public Criteria andIssueIntimeToCheckCountIsNotNull() {
            addCriterion("issue_intime_to_check_count is not null");
            return (Criteria) this;
        }

        public Criteria andIssueIntimeToCheckCountEqualTo(Integer value) {
            addCriterion("issue_intime_to_check_count =", value, "issueIntimeToCheckCount");
            return (Criteria) this;
        }

        public Criteria andIssueIntimeToCheckCountNotEqualTo(Integer value) {
            addCriterion("issue_intime_to_check_count <>", value, "issueIntimeToCheckCount");
            return (Criteria) this;
        }

        public Criteria andIssueIntimeToCheckCountGreaterThan(Integer value) {
            addCriterion("issue_intime_to_check_count >", value, "issueIntimeToCheckCount");
            return (Criteria) this;
        }

        public Criteria andIssueIntimeToCheckCountGreaterThanOrEqualTo(Integer value) {
            addCriterion("issue_intime_to_check_count >=", value, "issueIntimeToCheckCount");
            return (Criteria) this;
        }

        public Criteria andIssueIntimeToCheckCountLessThan(Integer value) {
            addCriterion("issue_intime_to_check_count <", value, "issueIntimeToCheckCount");
            return (Criteria) this;
        }

        public Criteria andIssueIntimeToCheckCountLessThanOrEqualTo(Integer value) {
            addCriterion("issue_intime_to_check_count <=", value, "issueIntimeToCheckCount");
            return (Criteria) this;
        }

        public Criteria andIssueIntimeToCheckCountIn(List<Integer> values) {
            addCriterion("issue_intime_to_check_count in", values, "issueIntimeToCheckCount");
            return (Criteria) this;
        }

        public Criteria andIssueIntimeToCheckCountNotIn(List<Integer> values) {
            addCriterion("issue_intime_to_check_count not in", values, "issueIntimeToCheckCount");
            return (Criteria) this;
        }

        public Criteria andIssueIntimeToCheckCountBetween(Integer value1, Integer value2) {
            addCriterion("issue_intime_to_check_count between", value1, value2, "issueIntimeToCheckCount");
            return (Criteria) this;
        }

        public Criteria andIssueIntimeToCheckCountNotBetween(Integer value1, Integer value2) {
            addCriterion("issue_intime_to_check_count not between", value1, value2, "issueIntimeToCheckCount");
            return (Criteria) this;
        }

        public Criteria andIssueIntimeCheckedCountIsNull() {
            addCriterion("issue_intime_checked_count is null");
            return (Criteria) this;
        }

        public Criteria andIssueIntimeCheckedCountIsNotNull() {
            addCriterion("issue_intime_checked_count is not null");
            return (Criteria) this;
        }

        public Criteria andIssueIntimeCheckedCountEqualTo(Integer value) {
            addCriterion("issue_intime_checked_count =", value, "issueIntimeCheckedCount");
            return (Criteria) this;
        }

        public Criteria andIssueIntimeCheckedCountNotEqualTo(Integer value) {
            addCriterion("issue_intime_checked_count <>", value, "issueIntimeCheckedCount");
            return (Criteria) this;
        }

        public Criteria andIssueIntimeCheckedCountGreaterThan(Integer value) {
            addCriterion("issue_intime_checked_count >", value, "issueIntimeCheckedCount");
            return (Criteria) this;
        }

        public Criteria andIssueIntimeCheckedCountGreaterThanOrEqualTo(Integer value) {
            addCriterion("issue_intime_checked_count >=", value, "issueIntimeCheckedCount");
            return (Criteria) this;
        }

        public Criteria andIssueIntimeCheckedCountLessThan(Integer value) {
            addCriterion("issue_intime_checked_count <", value, "issueIntimeCheckedCount");
            return (Criteria) this;
        }

        public Criteria andIssueIntimeCheckedCountLessThanOrEqualTo(Integer value) {
            addCriterion("issue_intime_checked_count <=", value, "issueIntimeCheckedCount");
            return (Criteria) this;
        }

        public Criteria andIssueIntimeCheckedCountIn(List<Integer> values) {
            addCriterion("issue_intime_checked_count in", values, "issueIntimeCheckedCount");
            return (Criteria) this;
        }

        public Criteria andIssueIntimeCheckedCountNotIn(List<Integer> values) {
            addCriterion("issue_intime_checked_count not in", values, "issueIntimeCheckedCount");
            return (Criteria) this;
        }

        public Criteria andIssueIntimeCheckedCountBetween(Integer value1, Integer value2) {
            addCriterion("issue_intime_checked_count between", value1, value2, "issueIntimeCheckedCount");
            return (Criteria) this;
        }

        public Criteria andIssueIntimeCheckedCountNotBetween(Integer value1, Integer value2) {
            addCriterion("issue_intime_checked_count not between", value1, value2, "issueIntimeCheckedCount");
            return (Criteria) this;
        }

        public Criteria andIssueNotsetToAssignCountIsNull() {
            addCriterion("issue_notset_to_assign_count is null");
            return (Criteria) this;
        }

        public Criteria andIssueNotsetToAssignCountIsNotNull() {
            addCriterion("issue_notset_to_assign_count is not null");
            return (Criteria) this;
        }

        public Criteria andIssueNotsetToAssignCountEqualTo(Integer value) {
            addCriterion("issue_notset_to_assign_count =", value, "issueNotsetToAssignCount");
            return (Criteria) this;
        }

        public Criteria andIssueNotsetToAssignCountNotEqualTo(Integer value) {
            addCriterion("issue_notset_to_assign_count <>", value, "issueNotsetToAssignCount");
            return (Criteria) this;
        }

        public Criteria andIssueNotsetToAssignCountGreaterThan(Integer value) {
            addCriterion("issue_notset_to_assign_count >", value, "issueNotsetToAssignCount");
            return (Criteria) this;
        }

        public Criteria andIssueNotsetToAssignCountGreaterThanOrEqualTo(Integer value) {
            addCriterion("issue_notset_to_assign_count >=", value, "issueNotsetToAssignCount");
            return (Criteria) this;
        }

        public Criteria andIssueNotsetToAssignCountLessThan(Integer value) {
            addCriterion("issue_notset_to_assign_count <", value, "issueNotsetToAssignCount");
            return (Criteria) this;
        }

        public Criteria andIssueNotsetToAssignCountLessThanOrEqualTo(Integer value) {
            addCriterion("issue_notset_to_assign_count <=", value, "issueNotsetToAssignCount");
            return (Criteria) this;
        }

        public Criteria andIssueNotsetToAssignCountIn(List<Integer> values) {
            addCriterion("issue_notset_to_assign_count in", values, "issueNotsetToAssignCount");
            return (Criteria) this;
        }

        public Criteria andIssueNotsetToAssignCountNotIn(List<Integer> values) {
            addCriterion("issue_notset_to_assign_count not in", values, "issueNotsetToAssignCount");
            return (Criteria) this;
        }

        public Criteria andIssueNotsetToAssignCountBetween(Integer value1, Integer value2) {
            addCriterion("issue_notset_to_assign_count between", value1, value2, "issueNotsetToAssignCount");
            return (Criteria) this;
        }

        public Criteria andIssueNotsetToAssignCountNotBetween(Integer value1, Integer value2) {
            addCriterion("issue_notset_to_assign_count not between", value1, value2, "issueNotsetToAssignCount");
            return (Criteria) this;
        }

        public Criteria andIssueNotsetToReformCountIsNull() {
            addCriterion("issue_notset_to_reform_count is null");
            return (Criteria) this;
        }

        public Criteria andIssueNotsetToReformCountIsNotNull() {
            addCriterion("issue_notset_to_reform_count is not null");
            return (Criteria) this;
        }

        public Criteria andIssueNotsetToReformCountEqualTo(Integer value) {
            addCriterion("issue_notset_to_reform_count =", value, "issueNotsetToReformCount");
            return (Criteria) this;
        }

        public Criteria andIssueNotsetToReformCountNotEqualTo(Integer value) {
            addCriterion("issue_notset_to_reform_count <>", value, "issueNotsetToReformCount");
            return (Criteria) this;
        }

        public Criteria andIssueNotsetToReformCountGreaterThan(Integer value) {
            addCriterion("issue_notset_to_reform_count >", value, "issueNotsetToReformCount");
            return (Criteria) this;
        }

        public Criteria andIssueNotsetToReformCountGreaterThanOrEqualTo(Integer value) {
            addCriterion("issue_notset_to_reform_count >=", value, "issueNotsetToReformCount");
            return (Criteria) this;
        }

        public Criteria andIssueNotsetToReformCountLessThan(Integer value) {
            addCriterion("issue_notset_to_reform_count <", value, "issueNotsetToReformCount");
            return (Criteria) this;
        }

        public Criteria andIssueNotsetToReformCountLessThanOrEqualTo(Integer value) {
            addCriterion("issue_notset_to_reform_count <=", value, "issueNotsetToReformCount");
            return (Criteria) this;
        }

        public Criteria andIssueNotsetToReformCountIn(List<Integer> values) {
            addCriterion("issue_notset_to_reform_count in", values, "issueNotsetToReformCount");
            return (Criteria) this;
        }

        public Criteria andIssueNotsetToReformCountNotIn(List<Integer> values) {
            addCriterion("issue_notset_to_reform_count not in", values, "issueNotsetToReformCount");
            return (Criteria) this;
        }

        public Criteria andIssueNotsetToReformCountBetween(Integer value1, Integer value2) {
            addCriterion("issue_notset_to_reform_count between", value1, value2, "issueNotsetToReformCount");
            return (Criteria) this;
        }

        public Criteria andIssueNotsetToReformCountNotBetween(Integer value1, Integer value2) {
            addCriterion("issue_notset_to_reform_count not between", value1, value2, "issueNotsetToReformCount");
            return (Criteria) this;
        }

        public Criteria andIssueNotsetToCheckCountIsNull() {
            addCriterion("issue_notset_to_check_count is null");
            return (Criteria) this;
        }

        public Criteria andIssueNotsetToCheckCountIsNotNull() {
            addCriterion("issue_notset_to_check_count is not null");
            return (Criteria) this;
        }

        public Criteria andIssueNotsetToCheckCountEqualTo(Integer value) {
            addCriterion("issue_notset_to_check_count =", value, "issueNotsetToCheckCount");
            return (Criteria) this;
        }

        public Criteria andIssueNotsetToCheckCountNotEqualTo(Integer value) {
            addCriterion("issue_notset_to_check_count <>", value, "issueNotsetToCheckCount");
            return (Criteria) this;
        }

        public Criteria andIssueNotsetToCheckCountGreaterThan(Integer value) {
            addCriterion("issue_notset_to_check_count >", value, "issueNotsetToCheckCount");
            return (Criteria) this;
        }

        public Criteria andIssueNotsetToCheckCountGreaterThanOrEqualTo(Integer value) {
            addCriterion("issue_notset_to_check_count >=", value, "issueNotsetToCheckCount");
            return (Criteria) this;
        }

        public Criteria andIssueNotsetToCheckCountLessThan(Integer value) {
            addCriterion("issue_notset_to_check_count <", value, "issueNotsetToCheckCount");
            return (Criteria) this;
        }

        public Criteria andIssueNotsetToCheckCountLessThanOrEqualTo(Integer value) {
            addCriterion("issue_notset_to_check_count <=", value, "issueNotsetToCheckCount");
            return (Criteria) this;
        }

        public Criteria andIssueNotsetToCheckCountIn(List<Integer> values) {
            addCriterion("issue_notset_to_check_count in", values, "issueNotsetToCheckCount");
            return (Criteria) this;
        }

        public Criteria andIssueNotsetToCheckCountNotIn(List<Integer> values) {
            addCriterion("issue_notset_to_check_count not in", values, "issueNotsetToCheckCount");
            return (Criteria) this;
        }

        public Criteria andIssueNotsetToCheckCountBetween(Integer value1, Integer value2) {
            addCriterion("issue_notset_to_check_count between", value1, value2, "issueNotsetToCheckCount");
            return (Criteria) this;
        }

        public Criteria andIssueNotsetToCheckCountNotBetween(Integer value1, Integer value2) {
            addCriterion("issue_notset_to_check_count not between", value1, value2, "issueNotsetToCheckCount");
            return (Criteria) this;
        }

        public Criteria andIssueNotsetCheckedCountIsNull() {
            addCriterion("issue_notset_checked_count is null");
            return (Criteria) this;
        }

        public Criteria andIssueNotsetCheckedCountIsNotNull() {
            addCriterion("issue_notset_checked_count is not null");
            return (Criteria) this;
        }

        public Criteria andIssueNotsetCheckedCountEqualTo(Integer value) {
            addCriterion("issue_notset_checked_count =", value, "issueNotsetCheckedCount");
            return (Criteria) this;
        }

        public Criteria andIssueNotsetCheckedCountNotEqualTo(Integer value) {
            addCriterion("issue_notset_checked_count <>", value, "issueNotsetCheckedCount");
            return (Criteria) this;
        }

        public Criteria andIssueNotsetCheckedCountGreaterThan(Integer value) {
            addCriterion("issue_notset_checked_count >", value, "issueNotsetCheckedCount");
            return (Criteria) this;
        }

        public Criteria andIssueNotsetCheckedCountGreaterThanOrEqualTo(Integer value) {
            addCriterion("issue_notset_checked_count >=", value, "issueNotsetCheckedCount");
            return (Criteria) this;
        }

        public Criteria andIssueNotsetCheckedCountLessThan(Integer value) {
            addCriterion("issue_notset_checked_count <", value, "issueNotsetCheckedCount");
            return (Criteria) this;
        }

        public Criteria andIssueNotsetCheckedCountLessThanOrEqualTo(Integer value) {
            addCriterion("issue_notset_checked_count <=", value, "issueNotsetCheckedCount");
            return (Criteria) this;
        }

        public Criteria andIssueNotsetCheckedCountIn(List<Integer> values) {
            addCriterion("issue_notset_checked_count in", values, "issueNotsetCheckedCount");
            return (Criteria) this;
        }

        public Criteria andIssueNotsetCheckedCountNotIn(List<Integer> values) {
            addCriterion("issue_notset_checked_count not in", values, "issueNotsetCheckedCount");
            return (Criteria) this;
        }

        public Criteria andIssueNotsetCheckedCountBetween(Integer value1, Integer value2) {
            addCriterion("issue_notset_checked_count between", value1, value2, "issueNotsetCheckedCount");
            return (Criteria) this;
        }

        public Criteria andIssueNotsetCheckedCountNotBetween(Integer value1, Integer value2) {
            addCriterion("issue_notset_checked_count not between", value1, value2, "issueNotsetCheckedCount");
            return (Criteria) this;
        }

        public Criteria andUpdateAtIsNull() {
            addCriterion("update_at is null");
            return (Criteria) this;
        }

        public Criteria andUpdateAtIsNotNull() {
            addCriterion("update_at is not null");
            return (Criteria) this;
        }

        public Criteria andUpdateAtEqualTo(Date value) {
            addCriterion("update_at =", value, "updateAt");
            return (Criteria) this;
        }

        public Criteria andUpdateAtNotEqualTo(Date value) {
            addCriterion("update_at <>", value, "updateAt");
            return (Criteria) this;
        }

        public Criteria andUpdateAtGreaterThan(Date value) {
            addCriterion("update_at >", value, "updateAt");
            return (Criteria) this;
        }

        public Criteria andUpdateAtGreaterThanOrEqualTo(Date value) {
            addCriterion("update_at >=", value, "updateAt");
            return (Criteria) this;
        }

        public Criteria andUpdateAtLessThan(Date value) {
            addCriterion("update_at <", value, "updateAt");
            return (Criteria) this;
        }

        public Criteria andUpdateAtLessThanOrEqualTo(Date value) {
            addCriterion("update_at <=", value, "updateAt");
            return (Criteria) this;
        }

        public Criteria andUpdateAtIn(List<Date> values) {
            addCriterion("update_at in", values, "updateAt");
            return (Criteria) this;
        }

        public Criteria andUpdateAtNotIn(List<Date> values) {
            addCriterion("update_at not in", values, "updateAt");
            return (Criteria) this;
        }

        public Criteria andUpdateAtBetween(Date value1, Date value2) {
            addCriterion("update_at between", value1, value2, "updateAt");
            return (Criteria) this;
        }

        public Criteria andUpdateAtNotBetween(Date value1, Date value2) {
            addCriterion("update_at not between", value1, value2, "updateAt");
            return (Criteria) this;
        }
    }

    public static class Criteria extends GeneratedCriteria {

        protected Criteria() {
            super();
        }
    }

    public static class Criterion {
        private String condition;

        private Object value;

        private Object secondValue;

        private boolean noValue;

        private boolean singleValue;

        private boolean betweenValue;

        private boolean listValue;

        private String typeHandler;

        public String getCondition() {
            return condition;
        }

        public Object getValue() {
            return value;
        }

        public Object getSecondValue() {
            return secondValue;
        }

        public boolean isNoValue() {
            return noValue;
        }

        public boolean isSingleValue() {
            return singleValue;
        }

        public boolean isBetweenValue() {
            return betweenValue;
        }

        public boolean isListValue() {
            return listValue;
        }

        public String getTypeHandler() {
            return typeHandler;
        }

        protected Criterion(String condition) {
            super();
            this.condition = condition;
            this.typeHandler = null;
            this.noValue = true;
        }

        protected Criterion(String condition, Object value, String typeHandler) {
            super();
            this.condition = condition;
            this.value = value;
            this.typeHandler = typeHandler;
            if (value instanceof List<?>) {
                this.listValue = true;
            } else {
                this.singleValue = true;
            }
        }

        protected Criterion(String condition, Object value) {
            this(condition, value, null);
        }

        protected Criterion(String condition, Object value, Object secondValue, String typeHandler) {
            super();
            this.condition = condition;
            this.value = value;
            this.secondValue = secondValue;
            this.typeHandler = typeHandler;
            this.betweenValue = true;
        }

        protected Criterion(String condition, Object value, Object secondValue) {
            this(condition, value, secondValue, null);
        }
    }
}