package com.longfor.longjian.houseqm.po.stat;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class StatHouseQmProjectUserWorkloadRepairerDailyStatExample {
    protected String orderByClause;

    protected boolean distinct;

    protected List<Criteria> oredCriteria;

    public StatHouseQmProjectUserWorkloadRepairerDailyStatExample() {
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

        public Criteria andUserIdIsNull() {
            addCriterion("user_id is null");
            return (Criteria) this;
        }

        public Criteria andUserIdIsNotNull() {
            addCriterion("user_id is not null");
            return (Criteria) this;
        }

        public Criteria andUserIdEqualTo(Integer value) {
            addCriterion("user_id =", value, "userId");
            return (Criteria) this;
        }

        public Criteria andUserIdNotEqualTo(Integer value) {
            addCriterion("user_id <>", value, "userId");
            return (Criteria) this;
        }

        public Criteria andUserIdGreaterThan(Integer value) {
            addCriterion("user_id >", value, "userId");
            return (Criteria) this;
        }

        public Criteria andUserIdGreaterThanOrEqualTo(Integer value) {
            addCriterion("user_id >=", value, "userId");
            return (Criteria) this;
        }

        public Criteria andUserIdLessThan(Integer value) {
            addCriterion("user_id <", value, "userId");
            return (Criteria) this;
        }

        public Criteria andUserIdLessThanOrEqualTo(Integer value) {
            addCriterion("user_id <=", value, "userId");
            return (Criteria) this;
        }

        public Criteria andUserIdIn(List<Integer> values) {
            addCriterion("user_id in", values, "userId");
            return (Criteria) this;
        }

        public Criteria andUserIdNotIn(List<Integer> values) {
            addCriterion("user_id not in", values, "userId");
            return (Criteria) this;
        }

        public Criteria andUserIdBetween(Integer value1, Integer value2) {
            addCriterion("user_id between", value1, value2, "userId");
            return (Criteria) this;
        }

        public Criteria andUserIdNotBetween(Integer value1, Integer value2) {
            addCriterion("user_id not between", value1, value2, "userId");
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

        public Criteria andIssueReformedCountIsNull() {
            addCriterion("issue_reformed_count is null");
            return (Criteria) this;
        }

        public Criteria andIssueReformedCountIsNotNull() {
            addCriterion("issue_reformed_count is not null");
            return (Criteria) this;
        }

        public Criteria andIssueReformedCountEqualTo(Integer value) {
            addCriterion("issue_reformed_count =", value, "issueReformedCount");
            return (Criteria) this;
        }

        public Criteria andIssueReformedCountNotEqualTo(Integer value) {
            addCriterion("issue_reformed_count <>", value, "issueReformedCount");
            return (Criteria) this;
        }

        public Criteria andIssueReformedCountGreaterThan(Integer value) {
            addCriterion("issue_reformed_count >", value, "issueReformedCount");
            return (Criteria) this;
        }

        public Criteria andIssueReformedCountGreaterThanOrEqualTo(Integer value) {
            addCriterion("issue_reformed_count >=", value, "issueReformedCount");
            return (Criteria) this;
        }

        public Criteria andIssueReformedCountLessThan(Integer value) {
            addCriterion("issue_reformed_count <", value, "issueReformedCount");
            return (Criteria) this;
        }

        public Criteria andIssueReformedCountLessThanOrEqualTo(Integer value) {
            addCriterion("issue_reformed_count <=", value, "issueReformedCount");
            return (Criteria) this;
        }

        public Criteria andIssueReformedCountIn(List<Integer> values) {
            addCriterion("issue_reformed_count in", values, "issueReformedCount");
            return (Criteria) this;
        }

        public Criteria andIssueReformedCountNotIn(List<Integer> values) {
            addCriterion("issue_reformed_count not in", values, "issueReformedCount");
            return (Criteria) this;
        }

        public Criteria andIssueReformedCountBetween(Integer value1, Integer value2) {
            addCriterion("issue_reformed_count between", value1, value2, "issueReformedCount");
            return (Criteria) this;
        }

        public Criteria andIssueReformedCountNotBetween(Integer value1, Integer value2) {
            addCriterion("issue_reformed_count not between", value1, value2, "issueReformedCount");
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

        public Criteria andIssueNewAssignNoReformCountIsNull() {
            addCriterion("issue_new_assign_no_reform_count is null");
            return (Criteria) this;
        }

        public Criteria andIssueNewAssignNoReformCountIsNotNull() {
            addCriterion("issue_new_assign_no_reform_count is not null");
            return (Criteria) this;
        }

        public Criteria andIssueNewAssignNoReformCountEqualTo(Integer value) {
            addCriterion("issue_new_assign_no_reform_count =", value, "issueNewAssignNoReformCount");
            return (Criteria) this;
        }

        public Criteria andIssueNewAssignNoReformCountNotEqualTo(Integer value) {
            addCriterion("issue_new_assign_no_reform_count <>", value, "issueNewAssignNoReformCount");
            return (Criteria) this;
        }

        public Criteria andIssueNewAssignNoReformCountGreaterThan(Integer value) {
            addCriterion("issue_new_assign_no_reform_count >", value, "issueNewAssignNoReformCount");
            return (Criteria) this;
        }

        public Criteria andIssueNewAssignNoReformCountGreaterThanOrEqualTo(Integer value) {
            addCriterion("issue_new_assign_no_reform_count >=", value, "issueNewAssignNoReformCount");
            return (Criteria) this;
        }

        public Criteria andIssueNewAssignNoReformCountLessThan(Integer value) {
            addCriterion("issue_new_assign_no_reform_count <", value, "issueNewAssignNoReformCount");
            return (Criteria) this;
        }

        public Criteria andIssueNewAssignNoReformCountLessThanOrEqualTo(Integer value) {
            addCriterion("issue_new_assign_no_reform_count <=", value, "issueNewAssignNoReformCount");
            return (Criteria) this;
        }

        public Criteria andIssueNewAssignNoReformCountIn(List<Integer> values) {
            addCriterion("issue_new_assign_no_reform_count in", values, "issueNewAssignNoReformCount");
            return (Criteria) this;
        }

        public Criteria andIssueNewAssignNoReformCountNotIn(List<Integer> values) {
            addCriterion("issue_new_assign_no_reform_count not in", values, "issueNewAssignNoReformCount");
            return (Criteria) this;
        }

        public Criteria andIssueNewAssignNoReformCountBetween(Integer value1, Integer value2) {
            addCriterion("issue_new_assign_no_reform_count between", value1, value2, "issueNewAssignNoReformCount");
            return (Criteria) this;
        }

        public Criteria andIssueNewAssignNoReformCountNotBetween(Integer value1, Integer value2) {
            addCriterion("issue_new_assign_no_reform_count not between", value1, value2, "issueNewAssignNoReformCount");
            return (Criteria) this;
        }

        public Criteria andIssueNewReformedCountIsNull() {
            addCriterion("issue_new_reformed_count is null");
            return (Criteria) this;
        }

        public Criteria andIssueNewReformedCountIsNotNull() {
            addCriterion("issue_new_reformed_count is not null");
            return (Criteria) this;
        }

        public Criteria andIssueNewReformedCountEqualTo(Integer value) {
            addCriterion("issue_new_reformed_count =", value, "issueNewReformedCount");
            return (Criteria) this;
        }

        public Criteria andIssueNewReformedCountNotEqualTo(Integer value) {
            addCriterion("issue_new_reformed_count <>", value, "issueNewReformedCount");
            return (Criteria) this;
        }

        public Criteria andIssueNewReformedCountGreaterThan(Integer value) {
            addCriterion("issue_new_reformed_count >", value, "issueNewReformedCount");
            return (Criteria) this;
        }

        public Criteria andIssueNewReformedCountGreaterThanOrEqualTo(Integer value) {
            addCriterion("issue_new_reformed_count >=", value, "issueNewReformedCount");
            return (Criteria) this;
        }

        public Criteria andIssueNewReformedCountLessThan(Integer value) {
            addCriterion("issue_new_reformed_count <", value, "issueNewReformedCount");
            return (Criteria) this;
        }

        public Criteria andIssueNewReformedCountLessThanOrEqualTo(Integer value) {
            addCriterion("issue_new_reformed_count <=", value, "issueNewReformedCount");
            return (Criteria) this;
        }

        public Criteria andIssueNewReformedCountIn(List<Integer> values) {
            addCriterion("issue_new_reformed_count in", values, "issueNewReformedCount");
            return (Criteria) this;
        }

        public Criteria andIssueNewReformedCountNotIn(List<Integer> values) {
            addCriterion("issue_new_reformed_count not in", values, "issueNewReformedCount");
            return (Criteria) this;
        }

        public Criteria andIssueNewReformedCountBetween(Integer value1, Integer value2) {
            addCriterion("issue_new_reformed_count between", value1, value2, "issueNewReformedCount");
            return (Criteria) this;
        }

        public Criteria andIssueNewReformedCountNotBetween(Integer value1, Integer value2) {
            addCriterion("issue_new_reformed_count not between", value1, value2, "issueNewReformedCount");
            return (Criteria) this;
        }

        public Criteria andDateDayIsNull() {
            addCriterion("date_day is null");
            return (Criteria) this;
        }

        public Criteria andDateDayIsNotNull() {
            addCriterion("date_day is not null");
            return (Criteria) this;
        }

        public Criteria andDateDayEqualTo(String value) {
            addCriterion("date_day =", value, "dateDay");
            return (Criteria) this;
        }

        public Criteria andDateDayNotEqualTo(String value) {
            addCriterion("date_day <>", value, "dateDay");
            return (Criteria) this;
        }

        public Criteria andDateDayGreaterThan(String value) {
            addCriterion("date_day >", value, "dateDay");
            return (Criteria) this;
        }

        public Criteria andDateDayGreaterThanOrEqualTo(String value) {
            addCriterion("date_day >=", value, "dateDay");
            return (Criteria) this;
        }

        public Criteria andDateDayLessThan(String value) {
            addCriterion("date_day <", value, "dateDay");
            return (Criteria) this;
        }

        public Criteria andDateDayLessThanOrEqualTo(String value) {
            addCriterion("date_day <=", value, "dateDay");
            return (Criteria) this;
        }

        public Criteria andDateDayLike(String value) {
            addCriterion("date_day like", value, "dateDay");
            return (Criteria) this;
        }

        public Criteria andDateDayNotLike(String value) {
            addCriterion("date_day not like", value, "dateDay");
            return (Criteria) this;
        }

        public Criteria andDateDayIn(List<String> values) {
            addCriterion("date_day in", values, "dateDay");
            return (Criteria) this;
        }

        public Criteria andDateDayNotIn(List<String> values) {
            addCriterion("date_day not in", values, "dateDay");
            return (Criteria) this;
        }

        public Criteria andDateDayBetween(String value1, String value2) {
            addCriterion("date_day between", value1, value2, "dateDay");
            return (Criteria) this;
        }

        public Criteria andDateDayNotBetween(String value1, String value2) {
            addCriterion("date_day not between", value1, value2, "dateDay");
            return (Criteria) this;
        }

        public Criteria andDateWeekIsNull() {
            addCriterion("date_week is null");
            return (Criteria) this;
        }

        public Criteria andDateWeekIsNotNull() {
            addCriterion("date_week is not null");
            return (Criteria) this;
        }

        public Criteria andDateWeekEqualTo(String value) {
            addCriterion("date_week =", value, "dateWeek");
            return (Criteria) this;
        }

        public Criteria andDateWeekNotEqualTo(String value) {
            addCriterion("date_week <>", value, "dateWeek");
            return (Criteria) this;
        }

        public Criteria andDateWeekGreaterThan(String value) {
            addCriterion("date_week >", value, "dateWeek");
            return (Criteria) this;
        }

        public Criteria andDateWeekGreaterThanOrEqualTo(String value) {
            addCriterion("date_week >=", value, "dateWeek");
            return (Criteria) this;
        }

        public Criteria andDateWeekLessThan(String value) {
            addCriterion("date_week <", value, "dateWeek");
            return (Criteria) this;
        }

        public Criteria andDateWeekLessThanOrEqualTo(String value) {
            addCriterion("date_week <=", value, "dateWeek");
            return (Criteria) this;
        }

        public Criteria andDateWeekLike(String value) {
            addCriterion("date_week like", value, "dateWeek");
            return (Criteria) this;
        }

        public Criteria andDateWeekNotLike(String value) {
            addCriterion("date_week not like", value, "dateWeek");
            return (Criteria) this;
        }

        public Criteria andDateWeekIn(List<String> values) {
            addCriterion("date_week in", values, "dateWeek");
            return (Criteria) this;
        }

        public Criteria andDateWeekNotIn(List<String> values) {
            addCriterion("date_week not in", values, "dateWeek");
            return (Criteria) this;
        }

        public Criteria andDateWeekBetween(String value1, String value2) {
            addCriterion("date_week between", value1, value2, "dateWeek");
            return (Criteria) this;
        }

        public Criteria andDateWeekNotBetween(String value1, String value2) {
            addCriterion("date_week not between", value1, value2, "dateWeek");
            return (Criteria) this;
        }

        public Criteria andDateMonthIsNull() {
            addCriterion("date_month is null");
            return (Criteria) this;
        }

        public Criteria andDateMonthIsNotNull() {
            addCriterion("date_month is not null");
            return (Criteria) this;
        }

        public Criteria andDateMonthEqualTo(String value) {
            addCriterion("date_month =", value, "dateMonth");
            return (Criteria) this;
        }

        public Criteria andDateMonthNotEqualTo(String value) {
            addCriterion("date_month <>", value, "dateMonth");
            return (Criteria) this;
        }

        public Criteria andDateMonthGreaterThan(String value) {
            addCriterion("date_month >", value, "dateMonth");
            return (Criteria) this;
        }

        public Criteria andDateMonthGreaterThanOrEqualTo(String value) {
            addCriterion("date_month >=", value, "dateMonth");
            return (Criteria) this;
        }

        public Criteria andDateMonthLessThan(String value) {
            addCriterion("date_month <", value, "dateMonth");
            return (Criteria) this;
        }

        public Criteria andDateMonthLessThanOrEqualTo(String value) {
            addCriterion("date_month <=", value, "dateMonth");
            return (Criteria) this;
        }

        public Criteria andDateMonthLike(String value) {
            addCriterion("date_month like", value, "dateMonth");
            return (Criteria) this;
        }

        public Criteria andDateMonthNotLike(String value) {
            addCriterion("date_month not like", value, "dateMonth");
            return (Criteria) this;
        }

        public Criteria andDateMonthIn(List<String> values) {
            addCriterion("date_month in", values, "dateMonth");
            return (Criteria) this;
        }

        public Criteria andDateMonthNotIn(List<String> values) {
            addCriterion("date_month not in", values, "dateMonth");
            return (Criteria) this;
        }

        public Criteria andDateMonthBetween(String value1, String value2) {
            addCriterion("date_month between", value1, value2, "dateMonth");
            return (Criteria) this;
        }

        public Criteria andDateMonthNotBetween(String value1, String value2) {
            addCriterion("date_month not between", value1, value2, "dateMonth");
            return (Criteria) this;
        }

        public Criteria andDateQuarterIsNull() {
            addCriterion("date_quarter is null");
            return (Criteria) this;
        }

        public Criteria andDateQuarterIsNotNull() {
            addCriterion("date_quarter is not null");
            return (Criteria) this;
        }

        public Criteria andDateQuarterEqualTo(String value) {
            addCriterion("date_quarter =", value, "dateQuarter");
            return (Criteria) this;
        }

        public Criteria andDateQuarterNotEqualTo(String value) {
            addCriterion("date_quarter <>", value, "dateQuarter");
            return (Criteria) this;
        }

        public Criteria andDateQuarterGreaterThan(String value) {
            addCriterion("date_quarter >", value, "dateQuarter");
            return (Criteria) this;
        }

        public Criteria andDateQuarterGreaterThanOrEqualTo(String value) {
            addCriterion("date_quarter >=", value, "dateQuarter");
            return (Criteria) this;
        }

        public Criteria andDateQuarterLessThan(String value) {
            addCriterion("date_quarter <", value, "dateQuarter");
            return (Criteria) this;
        }

        public Criteria andDateQuarterLessThanOrEqualTo(String value) {
            addCriterion("date_quarter <=", value, "dateQuarter");
            return (Criteria) this;
        }

        public Criteria andDateQuarterLike(String value) {
            addCriterion("date_quarter like", value, "dateQuarter");
            return (Criteria) this;
        }

        public Criteria andDateQuarterNotLike(String value) {
            addCriterion("date_quarter not like", value, "dateQuarter");
            return (Criteria) this;
        }

        public Criteria andDateQuarterIn(List<String> values) {
            addCriterion("date_quarter in", values, "dateQuarter");
            return (Criteria) this;
        }

        public Criteria andDateQuarterNotIn(List<String> values) {
            addCriterion("date_quarter not in", values, "dateQuarter");
            return (Criteria) this;
        }

        public Criteria andDateQuarterBetween(String value1, String value2) {
            addCriterion("date_quarter between", value1, value2, "dateQuarter");
            return (Criteria) this;
        }

        public Criteria andDateQuarterNotBetween(String value1, String value2) {
            addCriterion("date_quarter not between", value1, value2, "dateQuarter");
            return (Criteria) this;
        }

        public Criteria andDateYearIsNull() {
            addCriterion("date_year is null");
            return (Criteria) this;
        }

        public Criteria andDateYearIsNotNull() {
            addCriterion("date_year is not null");
            return (Criteria) this;
        }

        public Criteria andDateYearEqualTo(String value) {
            addCriterion("date_year =", value, "dateYear");
            return (Criteria) this;
        }

        public Criteria andDateYearNotEqualTo(String value) {
            addCriterion("date_year <>", value, "dateYear");
            return (Criteria) this;
        }

        public Criteria andDateYearGreaterThan(String value) {
            addCriterion("date_year >", value, "dateYear");
            return (Criteria) this;
        }

        public Criteria andDateYearGreaterThanOrEqualTo(String value) {
            addCriterion("date_year >=", value, "dateYear");
            return (Criteria) this;
        }

        public Criteria andDateYearLessThan(String value) {
            addCriterion("date_year <", value, "dateYear");
            return (Criteria) this;
        }

        public Criteria andDateYearLessThanOrEqualTo(String value) {
            addCriterion("date_year <=", value, "dateYear");
            return (Criteria) this;
        }

        public Criteria andDateYearLike(String value) {
            addCriterion("date_year like", value, "dateYear");
            return (Criteria) this;
        }

        public Criteria andDateYearNotLike(String value) {
            addCriterion("date_year not like", value, "dateYear");
            return (Criteria) this;
        }

        public Criteria andDateYearIn(List<String> values) {
            addCriterion("date_year in", values, "dateYear");
            return (Criteria) this;
        }

        public Criteria andDateYearNotIn(List<String> values) {
            addCriterion("date_year not in", values, "dateYear");
            return (Criteria) this;
        }

        public Criteria andDateYearBetween(String value1, String value2) {
            addCriterion("date_year between", value1, value2, "dateYear");
            return (Criteria) this;
        }

        public Criteria andDateYearNotBetween(String value1, String value2) {
            addCriterion("date_year not between", value1, value2, "dateYear");
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