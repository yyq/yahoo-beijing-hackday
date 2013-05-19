<!DOCTYPE html>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
    <meta name="viewport" content="width=device-width,minimum-scale=1.0,maximum-scale=1.0,user-scalable=no">
    <style type="text/css">
        body, html, #allmap {
            width: 100%;
            height: 100%;
            margin: 0;
        }

        #l-map {
            height: 100%;
            width: 78%;
            float: left;
            border-right: 2px solid #bcbcbc;
        }

        #r-result {
            height: 100%;
            width: 20%;
            float: left;
        }

        #main {
            width: 320px;
            min-height: 520px;
            margin: 0 auto;
        }

        #allmap {
            height: 500px;
        }
        li{
            list-style: none;
        }
        ul{
            padding: 0;
        }
    </style>
    <script type="text/javascript"
            src="http://api.map.baidu.com/api?v=1.5&ak=388fdcde2cef4925f985b3a374dda9df"></script>
    <title>可视区域内的搜素</title>
</head>
<body>
<div id="main">
    <div id="op" style="text-align: center">
        <button id="_fp">历史足迹</button>
        <button id="cur">当前位置</button>
        <button id="res">周边餐饮</button>
        <button id="enter">周边娱乐</button>
    </div>
    <div id="allmap"></div>
    <div id="warn"></div>
    <div id="foot_print"></div>
</div>
<script src="http://yui.yahooapis.com/3.10.1/build/yui/yui-min.js"></script>
<script type="text/javascript">
    (function (exports) {
        var _d = exports.document, _n = exports.navigator;
        var _warn = document.getElementById("warn");
        var x, y, local, map;

        function getLocation() {
            if (_n.geolocation) {
                _n.geolocation.getCurrentPosition(showPosition);
            }
            else {
                _warn.innerHTML = "Geolocation is not supported by this browser.";
            }
        }

        function showPosition(position) {
            x = position.coords.latitude;
            y = position.coords.longitude;
            map = new BMap.Map("allmap");
            var _point = new BMap.Point(y, x);
            map.centerAndZoom(_point, 14);
            local = new BMap.LocalSearch(map, {
                renderOptions:{map:map}
            });
            map.addOverlay(new BMap.Marker(_point));
            /* local.searchInBounds("银行", map.getBounds());

             map.addEventListener("dragend", function () {
             map.clearOverlays();
             local.searchInBounds("银行", map.getBounds());
             });*/
        }

        getLocation();
        var _cur = document.getElementById("cur");
        var _res = document.getElementById("res");
        var _enter = document.getElementById("enter");
        var _fp = document.getElementById("_fp");
        _cur.onclick = function () {
            getLocation();
        };
        _res.onclick = function () {
            //getLocation();
            map.reset();
            local.searchInBounds("餐饮", map.getBounds());
            map.addEventListener("dragend", function () {
                map.reset();
                map.clearOverlays();
                local.searchInBounds("餐饮", map.getBounds());
            });
        };
        _enter.onclick = function () {
            //getLocation();
            map.reset();
            local.searchInBounds("娱乐", map.getBounds());
            map.addEventListener("dragend", function () {
                map.reset();
                map.clearOverlays();
                local.searchInBounds("娱乐", map.getBounds());
            });
        };

        YUI().use('yql', 'node', function (Y) {
          /*  y= 40.68907;
            x= 74.0448;*/
            Y.YQL('select * from flickr.photos.search where has_geo="true" and lat="' + x + '" and lon="' + y + '" and api_key="4fb031bf5b2f138576d011ff37f31565";', function (r) {
                console.log(r);
                var contentNode = Y.Node.create('div');
                var photos = r.query.results.photo;
                var html = '<ul>';
                if (!(photos instanceof Array)) {
                    html += "<li><img width='320px' height='320px' src='http://farm" + photos.farm + ".static.flickr.com/" + photos.server + "/" + photos.id + "_" + photos.secret + ".jpg'/><span>"+photos.title+"</span></li>";
                } else {
                    for (var i in photos) {
                        var photo = photos[i];
                        html += "<li><img width='320px' height='320px' src='http://farm" + photo.farm + ".static.flickr.com/" + photo.server + "/" + photo.id + "_" + photo.secret + ".jpg'/> <span>"+photo.title+"</span></li>";
                        //console.log("<img src='http://farm"+photo.farm+".static.flickr.com/"+photo.server+"/"+photo.id+"_"+photo.secret+".jpg'/>");
                        //contentNode.append("<img src='http://farm"+photo.farm+".static.flickr.com/"+photo.server+"/"+photo.id+"_"+photo.secret+"'/>")
                    }

                }
                document.getElementById("foot_print").innerHTML = html+"</ul>";
                //N.one("body").append(contentNode);
                console.log(contentNode);

                //<img src="http://farm{$farm}.static.flickr.com/{$server}/{$id}_{$secret}.jpg"
            });
        });
    })(window);
</script>
</body>
</html>
