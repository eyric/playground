package org.example.dal.entity;

import cn.org.atool.fluent.mybatis.annotation.FluentMybatis;
import cn.org.atool.fluent.mybatis.annotation.TableField;
import cn.org.atool.fluent.mybatis.annotation.TableId;
import cn.org.atool.fluent.mybatis.base.RichEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serial;

/**
 * DemoEntity: 数据映射实体定义
 *
 * @author Powered By Fluent Mybatis
 */
@SuppressWarnings({"rawtypes", "unchecked"})
@Data
@Accessors(
    chain = true
)
@EqualsAndHashCode(
    callSuper = false
)
@AllArgsConstructor
@NoArgsConstructor
@FluentMybatis(
    table = "demo",
    schema = "playground"
)
public class DemoEntity extends RichEntity {

  @Serial
  private static final long serialVersionUID = 1L;

  @TableId(
      value = "id",
      desc = "ID"
  )
  private Long id;

  @TableField(
      value = "name",
      desc = "名称"
  )
  private String name;

  @Override
  public final Class entityClass() {
    return DemoEntity.class;
  }
}
