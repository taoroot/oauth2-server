<template>
  <div>
    <ul class="icon-list">
      <li><span><i class="el-icon-position" /><span class="icon-name">80 %</span></span></li>
      <li><span><i class="el-icon-s-opportunity" /><span class="icon-name">80 %</span></span></li>
      <li><span><i class="el-icon-s-data" /><span class="icon-name">80 %</span></span></li>
      <li><span><i class="el-icon-s-check" /><span class="icon-name">80 %</span></span></li>
      <li><span><i class="el-icon-umbrella" /><span class="icon-name">80 %</span></span></li>
      <li><span><i class="el-icon-paperclip" /><span class="icon-name">80 %</span></span></li>
      <li><span><i class="el-icon-collection" /><span class="icon-name">80 %</span></span></li>
      <li><span><i class="el-icon-table-lamp" /><span class="icon-name">80 %</span></span></li>
      <li><span><i class="el-icon-shopping-bag-1" /><span class="icon-name">80 %</span></span></li>
    </ul>
  </div>
</template>

<script>
export default {

}
</script>

<style lang="scss" scoped>
ul.icon-list {
  overflow: hidden;
  list-style: none;
  padding: 0!important;
  border: solid 1px #eaeefb;
  border-radius: 4px;
}

.icon-list li {
  float: left;
  width: 33%;
  text-align: center;
  height: 120px;
  line-height: 120px;
  color: #666;
  font-size: 13px;
  border-right: 1px solid #eee;
  border-bottom: 1px solid #eee;
  margin-right: -1px;
  margin-bottom: -1px;

  &::after {
    display: inline-block;
    content: "";
    height: 100%;
    vertical-align: middle;
  }

  span {
    display: inline-block;
    line-height: normal;
    vertical-align: middle;
    font-family: "Helvetica Neue", Helvetica, "PingFang SC", "Hiragino Sans GB",
      "Microsoft YaHei", SimSun, sans-serif;
    color: #99a9bf;
    transition: color 0.15s linear;
  }

  i {
    display: block;
    font-size: 40px;
    margin-bottom: 15px;
    color: #606266;
    transition: color 0.15s linear;
  }

  .icon-name {
    display: inline-block;
    padding: 0 3px;
    height: 1em;
  }

  &:hover {
    span,
    i {
      color: rgb(92, 182, 255);
    }
  }
}
</style>
