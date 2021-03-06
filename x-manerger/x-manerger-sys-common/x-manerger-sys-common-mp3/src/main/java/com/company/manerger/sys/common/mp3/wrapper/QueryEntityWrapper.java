package com.company.manerger.sys.common.mp3.wrapper;


import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.ArrayUtils;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.ReflectionKit;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.util.*;
import java.util.function.BiPredicate;
import java.util.function.Function;
import java.util.stream.Collectors;

public class QueryEntityWrapper<T> extends QueryWrapper<T> {
    protected Class<T> entityClass;
    private Map<String, String> attrFieldMap;
    private String tableAlias = "";

    public QueryEntityWrapper(){

    }

    public QueryEntityWrapper(Class<T> entityClass){
        this.entityClass = entityClass;
        initFieldMap();
    }

    public void initFieldMap() {
        /* 注意，传入查询参数 */
        if (this.entityClass == null) {
            this.entityClass = (Class<T>) getTClass();
            initFieldMap();
        }
        attrFieldMap = new HashMap<String, String>();
        List<Field> allField = getAllFields(entityClass);
        for (Field field : allField) {
            /* 获取注解属性，自定义字段 */
            TableField tableField = field.getAnnotation(TableField.class);
            if (tableField != null) {
                String fieldName = field.getName();
                if (StringUtils.isNotEmpty(tableField.el())) {
                    fieldName = tableField.el();
                }
                if (StringUtils.isNotEmpty(tableField.value())) {
                    String columnName = tableField.value();
                    attrFieldMap.put(fieldName, columnName);
                }
            } else {
                String fieldName = field.getName();
                String columnName = StringUtils.camelToUnderline(fieldName);
                attrFieldMap.put(fieldName, columnName);
            }
        }
    }

    /**
     * 获取该类的所有属性列表
     * @param clazz
     * @return
     */
    public static List<Field> getAllFields(Class<?> clazz) {
        List<Field> fieldList = ReflectionKit.getFieldList(clazz);
        if (CollectionUtils.isNotEmpty(fieldList)) {
            Iterator<Field> iterator = fieldList.iterator();
            while (iterator.hasNext()) {
                Field field = iterator.next();
                /* 过滤注解非表字段属性 */
                TableField tableField = field.getAnnotation(TableField.class);
                if (tableField != null && !tableField.exist()) {
                    iterator.remove();
                }
            }
        }
        return fieldList;
    }

    /**
     *  获取传入的泛型类
     * @return
     */
    public Class<T> getTClass() {
        @SuppressWarnings("unchecked")
        Class<T> tClass = (Class<T>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
        return tClass;
    }

    public String getTableAlias() {
        return tableAlias;
    }

    public void setTableAlias(String tableAlias) {
        this.tableAlias = tableAlias;
    }

    private String handleColumn(String column) {
        if (attrFieldMap != null && attrFieldMap.containsKey(column)) {
            column = attrFieldMap.get(column);
        }
        // 处理表前缀
        if (column != null && !column.contains(".") && !column.trim().equals("1") && !StringUtils.isEmpty(tableAlias)) {
            column = tableAlias + "." + column;
        }
        return column;
    }

    @Override
    public <V> QueryWrapper<T> allEq(boolean condition, Map<String, V> params, boolean null2IsNull) {
        return super.allEq(condition, params, null2IsNull);
    }

    @Override
    public <V> QueryWrapper<T> allEq(boolean condition, BiPredicate<String, V> filter, Map<String, V> params, boolean null2IsNull) {
        return super.allEq(condition, filter, params, null2IsNull);
    }

    @Override
    public QueryWrapper<T> eq(boolean condition, String column, Object val) {
        return super.eq(condition, handleColumn(column), val);
    }

    @Override
    public QueryWrapper<T> ne(boolean condition, String column, Object val) {
        return super.ne(condition, handleColumn(column), val);
    }

    @Override
    public QueryWrapper<T> gt(boolean condition, String column, Object val) {
        return super.gt(condition, handleColumn(column), val);
    }

    @Override
    public QueryWrapper<T> ge(boolean condition, String column, Object val) {
        return super.ge(condition, handleColumn(column), val);
    }

    @Override
    public QueryWrapper<T> lt(boolean condition, String column, Object val) {
        return super.lt(condition, handleColumn(column), val);
    }

    @Override
    public QueryWrapper<T> le(boolean condition, String column, Object val) {
        return super.le(condition, handleColumn(column), val);
    }

    @Override
    public QueryWrapper<T> like(boolean condition, String column, Object val) {
        return super.like(condition, handleColumn(column), val);
    }

    @Override
    public QueryWrapper<T> notLike(boolean condition, String column, Object val) {
        return super.notLike(condition, handleColumn(column), val);
    }

    @Override
    public QueryWrapper<T> likeLeft(boolean condition, String column, Object val) {
        return super.likeLeft(condition, handleColumn(column), val);
    }

    @Override
    public QueryWrapper<T> likeRight(boolean condition, String column, Object val) {
        return super.likeRight(condition, handleColumn(column), val);
    }

    @Override
    public QueryWrapper<T> between(boolean condition, String column, Object val1, Object val2) {
        return super.between(condition, handleColumn(column), val1, val2);
    }

    @Override
    public QueryWrapper<T> notBetween(boolean condition, String column, Object val1, Object val2) {
        return super.notBetween(condition, handleColumn(column), val1, val2);
    }

    @Override
    public QueryWrapper<T> and(boolean condition, Function<QueryWrapper<T>, QueryWrapper<T>> func) {
        return super.and(condition, func);
    }

    @Override
    public QueryWrapper<T> or(boolean condition, Function<QueryWrapper<T>, QueryWrapper<T>> func) {
        return super.or(condition, func);
    }

    @Override
    public QueryWrapper<T> nested(boolean condition, Function<QueryWrapper<T>, QueryWrapper<T>> func) {
        return super.nested(condition, func);
    }

    @Override
    public QueryWrapper<T> or(boolean condition) {
        return super.or(condition);
    }

    @Override
    public QueryWrapper<T> apply(boolean condition, String applySql, Object... value) {
        return super.apply(condition, applySql, value);
    }

    @Override
    public QueryWrapper<T> last(boolean condition, String lastSql) {
        return super.last(condition, lastSql);
    }

    @Override
    public QueryWrapper<T> exists(boolean condition, String existsSql) {
        return super.exists(condition, existsSql);
    }

    @Override
    public QueryWrapper<T> notExists(boolean condition, String notExistsSql) {
        return super.notExists(condition, notExistsSql);
    }

    @Override
    public QueryWrapper<T> isNull(boolean condition, String column) {
        return super.isNull(condition, handleColumn(column));
    }

    @Override
    public QueryWrapper<T> isNotNull(boolean condition, String column) {
        return super.isNotNull(condition, handleColumn(column));
    }

    @Override
    public QueryWrapper<T> in(boolean condition, String column, Collection<?> coll) {
        return super.in(condition, handleColumn(column), coll);
    }

    @Override
    public QueryWrapper<T> notIn(boolean condition, String column, Collection<?> coll) {
        return super.notIn(condition, handleColumn(column), coll);
    }

    @Override
    public QueryWrapper<T> inSql(boolean condition, String column, String inValue) {
        return super.inSql(condition, handleColumn(column), inValue);
    }

    @Override
    public QueryWrapper<T> notInSql(boolean condition, String column, String inValue) {
        return super.notInSql(condition, handleColumn(column), inValue);
    }

    @Override
    public QueryWrapper<T> groupBy(boolean condition, String... columns) {
        if (ArrayUtils.isEmpty(columns)) {
            return super.groupBy(condition, columns);
        }
        String[] _columns = Arrays.stream(columns)
                .map(item->handleColumn(item))
                .collect(Collectors.toList())
                .toArray(new String[columns.length]);
        return super.groupBy(condition, _columns);
    }

    @Override
    public QueryWrapper<T> orderBy(boolean condition, boolean isAsc, String... columns) {
        if (ArrayUtils.isEmpty(columns)) {
            return super.orderBy(condition, isAsc, columns);
        }
        String[] _columns = Arrays.stream(columns)
                            .map(item->handleColumn(item))
                            .collect(Collectors.toList())
                            .toArray(new String[columns.length]);
        return super.orderBy(condition, isAsc, _columns);
    }

    @Override
    public QueryWrapper<T> having(boolean condition, String sqlHaving, Object... params) {
        return super.having(condition, sqlHaving, params);
    }
}
