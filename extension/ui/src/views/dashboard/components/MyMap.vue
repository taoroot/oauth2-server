<template>
  <div id="map-container" />
</template>

<script>
export default {
  name: 'DeviceMap',
  data() {
    return {
      map: {}
    }
  },
  mounted() {
    this.newMap() // 初始化Map
    this.IconMap() // 图标
    this.addMaker() // 添加锚点
  },
  methods: {
    newMap() {
      this.map = new AMap.Map('map-container', {
        // 'map-location'是对应页面盒子的id
        // mapStyle: 'amap://styles/f2b739003c0aa50be59e4f10e2c08e6c', // 地图样式
        mapStyle: 'amap://styles/whitesmoke',
        resizeEnable: true, // 自适应大小
        zoom: 16, // 初始视窗
        viewMode: '3D',
        pitch: 30,
        lang: 'zh_cn',
        center: [119.51, 29.1325],
        isHotspot: false // 取消热点hover
      })
      this.map.on('hotspotover', () => {
        // hover
      })
    },
    IconMap() {
      this.icon = new AMap.Icon({
        size: new AMap.Size(22, 22), // 图标尺寸
        image: require('@/assets/pic/null_addr@2x.png'), // Icon的图像
        imageSize: new AMap.Size(22, 22)
      })
      this.greenicon = new AMap.Icon({
        size: new AMap.Size(22, 22), // 图标尺寸
        image: require('@/assets/pic/green_addr@2x.png'), // Icon的图像
        imageSize: new AMap.Size(22, 22)
      })
      this.yellowicon = new AMap.Icon({
        size: new AMap.Size(22, 22), // 图标尺寸
        image: require('@/assets/pic/yellow_addr@2x.png'), // Icon的图像
        imageSize: new AMap.Size(22, 22)
      })
      this.orangeicon = new AMap.Icon({
        size: new AMap.Size(22, 22), // 图标尺寸
        image: require('@/assets/pic/orange_addr@2x.png'), // Icon的图像
        imageSize: new AMap.Size(22, 22)
      })
      this.redicon = new AMap.Icon({
        size: new AMap.Size(22, 22), // 图标尺寸
        image: require('@/assets/pic/red_addr@2x.png'), // Icon的图像
        imageSize: new AMap.Size(22, 22)
      })
    },
    addMaker() {
      var marker = new AMap.Marker({
        position: new AMap.LngLat(119.514, 29.1325),
        icon: this.orangeicon,
        offset: new AMap.Pixel(-12, -30)
      })

      // 实例化信息窗体
      var title =
        '设备: BHBD001 状态: <span style="font-size:11px;color:green;">在线</span>'
      var content = []
      content.push('地址：浙江省金华市金东区金义都市新区金山大道南658号金山科创园305')
      content.push('电话：0579-82115762')
      content.push("<a href='#'>详细信息</a>")

      var infoWindow = new AMap.InfoWindow({
        // 创建信息窗体
        isCustom: true, // 使用自定义窗体
        content: this.createInfoWindow(title, content.join('<br/>')), // 信息窗体的内容可以是任意html片段
        offset: new AMap.Pixel(16, -45)
      })
      var onMarkerClick = (e) => {
        infoWindow.open(this.map, e.target.getPosition())
      }
      this.map.add(marker)
      marker.on('click', onMarkerClick)
      this.map.add(marker)
    },

    createInfoWindow(title, content) {
      var info = document.createElement('div')
      info.className = 'custom-info input-card content-window-card'

      // 可以通过下面的方式修改自定义窗体的宽高
      // info.style.width = "400px";
      // 定义顶部标题
      var top = document.createElement('div')
      var titleD = document.createElement('div')
      var closeX = document.createElement('img')
      top.className = 'info-top'
      titleD.innerHTML = title
      closeX.src = 'https://webapi.amap.com/images/close2.gif'
      closeX.onclick = () => this.map.clearInfoWindow

      top.appendChild(titleD)
      top.appendChild(closeX)
      info.appendChild(top)

      // 定义中部内容
      var middle = document.createElement('div')
      middle.className = 'info-middle'
      middle.style.backgroundColor = 'white'
      middle.innerHTML = content
      info.appendChild(middle)

      // 定义底部内容
      var bottom = document.createElement('div')
      bottom.className = 'info-bottom'
      bottom.style.position = 'relative'
      bottom.style.top = '0px'
      bottom.style.margin = '0 auto'
      var sharp = document.createElement('img')
      sharp.src = 'https://webapi.amap.com/images/sharp.png'
      bottom.appendChild(sharp)
      info.appendChild(bottom)
      return info
    }
  }
}
</script>

<!-- Add "scoped" attribute to limit CSS to this component only -->
<style  scoped>
#map-container {
  width: 100%;
  min-height: calc(100vh - 134px );
}
</style>
<style >
.content-window-card {
  position: relative;
  box-shadow: none;
  bottom: 0;
  left: 0;
  width: auto;
  padding: 0;
}

.content-window-card p {
  height: 2rem;
}

.info-top {
  position: relative;
  background: none repeat scroll 0 0 #f9f9f9;
  border-bottom: 1px solid #ccc;
  border-radius: 5px 5px 0 0;
}

.info-top div {
  display: inline-block;
  color: #333333;
  font-size: 14px;
  font-weight: bold;
  line-height: 31px;
  padding: 0 10px;
}

.info-top img {
  position: absolute;
  top: 10px;
  right: 10px;
  transition-duration: 0.25s;
}

.info-top img:hover {
  box-shadow: 0px 0px 5px #000;
}

.info-middle {
  font-size: 12px;
  padding: 10px 6px;
  line-height: 20px;
}

.info-bottom {
  height: 0px;
  width: 100%;
  clear: both;
  text-align: center;
}

.info-bottom img {
  position: relative;
  z-index: 104;
}

span {
  margin-left: 5px;
  font-size: 11px;
}

.info-middle img {
  float: left;
  margin-right: 6px;
}

</style>
