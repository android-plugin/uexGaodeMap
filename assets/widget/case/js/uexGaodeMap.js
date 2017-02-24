if (UNIT_TEST) {
    var isOnDownloadExecute = false;
    var isMapLoaded = false;
    var isDownloaded = false;
    var cityName = null;
    uexGaodeMap.onMapLoadedListener = function () {
        isMapLoaded = true;
    }
    var uexGaodeMapCase = {
            "open": function () {
                var width = document.documentElement.clientWidth;
                var params = {
                    left: 0,
                    top: 50,
                    width: width,
                    height: 600,
                    isScrollWithWeb: false,
                    longitude: 114.402815,
                    latitude: 30.475798
                };
                var json = JSON.stringify(params);
                uexGaodeMap.open(json);
                UNIT_TEST.assertDelay(true, 3000);
            },
            "onMapLoadedListener": function () {
                if (isMapLoaded) {
                    UNIT_TEST.log("[test  onMapLoadedListener]: success");
                    UNIT_TEST.assert(true);
                } else {
                    UNIT_TEST.assert(false);
                }
            },

            "setMapType ": function () {
                var params = {
                    type: 1
                };
                var json = JSON.stringify(params);
                uexGaodeMap.setMapType(json);
                UNIT_TEST.assertDelay(true, 2000);
            },
            "setTrafficEnabled": function () {
                var params = {
                    type: 1
                };
                var json = JSON.stringify(params);
                uexGaodeMap.setTrafficEnabled(json);
                UNIT_TEST.assertDelay(true, 2000);
            },
            "setCenter": function () {
                var params = {
                    longitude: 114.402815,
                    latitude: 30.475798
                };
                var json = JSON.stringify(params);
                uexGaodeMap.setCenter(json);
                UNIT_TEST.assertDelay(true, 3000);
            },
            "setZoomLevel": function () {
                var params = {
                    level: 15
                };
                var json = JSON.stringify(params);
                uexGaodeMap.setZoomLevel(json);
                UNIT_TEST.assertDelay(true, 2000);
            },
            "zoomIn": function () {
                uexGaodeMap.zoomIn();
                UNIT_TEST.assertDelay(true, 3000);
            },
            "zoomOut": function () {
                uexGaodeMap.zoomOut();
                UNIT_TEST.assertDelay(true, 3000);
            },
            "rotate": function () {
                var params = {
                    angle: 90
                };
                var json = JSON.stringify(params);
                uexGaodeMap.rotate(json);
                UNIT_TEST.assertDelay(true, 3000);
            },
            "overlook": function () {
                var params = {
                    angle: 23
                };
                var json = JSON.stringify(params);
                uexGaodeMap.overlook(json);
                UNIT_TEST.assertDelay(true);
            },
            "setZoomEnable": function () {
                var params = {
                    type: 1
                };
                var json = JSON.stringify(params);
                uexGaodeMap.setZoomEnable(json);
                UNIT_TEST.assertDelay(true);
            },
            "setRotateEnable": function () {
                var params = {
                    type: 1
                };
                var json = JSON.stringify(params);
                uexGaodeMap.setRotateEnable(json);
                UNIT_TEST.assertDelay(true);
            },
            "setCompassEnable": function () {
                var params = {
                    type: 1
                };
                var json = JSON.stringify(params);
                uexGaodeMap.setCompassEnable(json);
                UNIT_TEST.assertDelay(true);
            },
            "setScrollEnable": function () {
                var params = {
                    type: 1
                };
                var json = JSON.stringify(params);
                uexGaodeMap.setScrollEnable(json);
                UNIT_TEST.assertDelay(true);
            },
            "drivingRouteSearch": function () {
                uexGaodeMap.transitRouteSearch({
                    origin: {
                        latitude: 39.910267,
                        longitude: 116.370888
                    },
                    destination: {
                        latitude: 39.989872,
                        longitude: 116.481956
                    },
                    city: "北京市",
                    strategy: 3,
                }, function (error, data) {
                    if (!error) {
                        alert("路径规划结果: " + JSON.stringify(data));
                    } else {
                        alert("路径规划失败: " + data);
                    }
                    UNIT_TEST.assert(!error);
                });
            },
            "walkingRouteSearch": function () {
                uexGaodeMap.walkingRouteSearch({
                    origin: {
                        latitude: 39.910267,
                        longitude: 116.370888
                    },
                    destination: {
                        latitude: 39.989872,
                        longitude: 116.481956
                    }
                }, function (error, data) {
                    if (!error) {
                        alert("路径规划结果: " + JSON.stringify(data));
                    } else {
                        alert("路径规划失败: " + data);
                    }
                    UNIT_TEST.assert(!error);
                });
            },
            "ridingRouteSearch": function () {
                uexGaodeMap.ridingRouteSearch({
                    origin: {
                        latitude: 39.910267,
                        longitude: 116.370888
                    },
                    destination: {
                        latitude: 39.989872,
                        longitude: 116.481956
                    }
                }, function (error, data) {
                    if (!error) {
                        alert("路径规划结果: " + JSON.stringify(data));
                    } else {
                        alert("路径规划失败: " + data);
                    }
                    UNIT_TEST.assert(!error);
                });
            },
            "transitRouteSearch": function () {
                uexGaodeMap.transitRouteSearch({
                    origin: {
                        latitude: 39.910267,
                        longitude: 116.370888
                    },
                    destination: {
                        latitude: 39.989872,
                        longitude: 116.481956
                    },
                    city: "北京市",
                    strategy: 3,
                }, function (error, data) {
                    if (!error) {
                        alert("路径规划结果: " + JSON.stringify(data));
                    } else {
                        alert("路径规划失败: " + data);
                    }
                    UNIT_TEST.assert(!error);
                });
            },
            "addMarkersOverlay": function () {
                var param = [
                    {
                        id: 10001,
                        longitude: 114.402965,
                        latitude: 30.475845,
                        icon: "res://mark.png",
                        bubble: {
                            title: "title1",
                            subTitle: "subTitle1"
                        }
                    },
                    {
                        id: 10002,
                        longitude: 114.409308,
                        latitude: 30.476229,
                        bubble: {
                            title: "title2",
                            subTitle: "subTitle2"
                        }
                    }
                ];
                var markerInfos = JSON.stringify(param);
                var markers = uexGaodeMap.addMarkersOverlay(markerInfos);
                UNIT_TEST.log("[addMarkersOverlay return data]" + JSON.stringify(markers) + markers.length);
                if (markers.length != 2) {
                    UNIT_TEST.assert(false);
                } else {
                    UNIT_TEST.assert(true);
                }

            }

            ,
            "setMarkerOverlay": function () {
                var params = {
                    id: 10002,
                    bubble: {
                        title: "change-title",
                        subTitle: "change-subTitle"
                    }
                };
                uexGaodeMap.setMarkerOverlay(params);
                UNIT_TEST.assertDelay(true, 2000);
            }

            ,
            "addPolylineOverlay": function () {
                var json = {
                    id: 10004,
                    fillColor: "#f00",
                    lineWidth: "10.0",
                    property: [
                        {
                            longitude: "114.402965",
                            latitude: "30.475845"
                        },
                        {
                            longitude: "114.502965",
                            latitude: "30.475845"
                        },
                        {
                            longitude: "114.402965",
                            latitude: "30.375845"
                        }
                    ]
                };
                var polylineInfo = JSON.stringify(json);
                var overlay = uexGaodeMap.addPolylineOverlay(polylineInfo);
                if (!overlay) {
                    UNIT_TEST.assert(false);
                } else {
                    UNIT_TEST.assertDelay(true);
                }

            }

            ,
            "addArcOverlay": function () {
                if (uexWidgetOne.platformName.indexOf('android') > -1) {
                    var jsonstr = {
                        id: 152,
                        strokeColor: "#f00",
                        lineWidth: "12.0",
                        start: {
                            longitude: "114.402965",
                            latitude: "30.475845"
                        },
                        center: {
                            longitude: "114.502965",
                            latitude: "30.475845"
                        },
                        end: {
                            longitude: "114.402965",
                            latitude: "30.375845"
                        }
                    };
                    var data = JSON.stringify(jsonstr);
                    var overlay = uexGaodeMap.addArcOverlay(data);
                    if (!overlay) {
                        UNIT_TEST.assert(false);
                    } else {
                        UNIT_TEST.assertDelay(true);
                    }
                } else {
                    UNIT_TEST.log("addArcOverlay仅支持安卓，iOS请忽略!");
                    UNIT_TEST.assert(true);
                }
            }

            ,
            "addCircleOverlay": function () {
                var jsonstr = {
                    id: 153,
                    longitude: "114.402965",
                    latitude: "30.375845",
                    radius: "1000",
                    fillColor: "#4169E1",
                    strokeColor: "#990033",
                    lineWidth: "4"
                };
                var data = JSON.stringify(jsonstr);
                var overlay = uexGaodeMap.addCircleOverlay(data);
                if (!overlay) {
                    UNIT_TEST.assert(false);
                } else {
                    UNIT_TEST.assertDelay(true);
                }
            }

            ,
            "addPolygonOverlay": function () {
                var jsonstr = {
                    id: 154,
                    fillColor: "#990033",
                    strokeColor: "#990033",
                    lineWidth: "2.0",
                    property: [
                        {
                            longitude: "114.402965",
                            latitude: "30.375845"
                        },
                        {
                            longitude: "114.404965",
                            latitude: "30.375845"
                        },
                        {
                            longitude: "114.402965",
                            latitude: "30.385845"
                        },
                        {
                            longitude: "114.403965",
                            latitude: "30.375845"
                        }
                    ]
                };
                var data = JSON.stringify(jsonstr);
                var overlay = uexGaodeMap.addPolygonOverlay(data);
                if (!overlay) {
                    UNIT_TEST.assert(false);
                } else {
                    UNIT_TEST.assertDelay(true);
                }
            }

            ,
            "addGroundOverlay": function () {
                var jsonstr = {
                    id: 155,
                    imageUrl: "http://img0.bdstatic.com/img/image/9baf75d938553886ce515def29441ed31409109131.jpg",
                    transparency: "0.5",
                    property: [
                        {
                            longitude: "114.402165",
                            latitude: "30.374845"
                        },
                        {
                            longitude: "114.502165",
                            latitude: "30.474845"
                        }
                    ]
                };
                var data = JSON.stringify(jsonstr);
                var overlay = uexGaodeMap.addGroundOverlay(data);
                if (!overlay) {
                    UNIT_TEST.assert(false);
                } else {
                    UNIT_TEST.assert(true);
                }
            }

            ,
            "poiSearch": function () {
                var jsonstr = {
                    city: "武汉",
                    poiTypeSet: "加油站",
                    searchBound: {
                        type: "circle",
                        dataInfo: {
                            center: {
                                longitude: 114.402815,
                                latitude: 30.475798
                            },
                            radius: 3000,
                            isDistanceSort: true
                        }
                    }
                };
                var data = JSON.stringify(jsonstr);
                uexGaodeMap.poiSearch(data, function (error, data) {
                    if (!error) {
                        UNIT_TEST.assert(true);
                        UNIT_TEST.log("[POISearch data]" + JSON.stringify(data));
                    } else {
                        UNIT_TEST.assertDelay(false);
                    }
                });
            }

            ,
            "geocode": function () {
                var jsonstr = {
                    city: "武汉",
                    address: "光谷软件园C6栋"
                };
                var json = JSON.stringify(jsonstr);
                uexGaodeMap.geocode(json, function (error, data) {
                    if (!error) {
                        UNIT_TEST.assert(true);
                        UNIT_TEST.log("[geocode data]" + JSON.stringify(data));
                    } else {
                        UNIT_TEST.assert(false);
                    }
                });

            }

            ,
            "reverseGeocode": function () {
                var jsonstr = {
                    longitude: 114.402815,
                    latitude: 30.475798
                };
                var json = JSON.stringify(jsonstr);
                uexGaodeMap.reverseGeocode(json, function (error, data) {
                    if (!error) {
                        UNIT_TEST.assert(true);
                        UNIT_TEST.log("[reverseGeocode data]" + JSON.stringify(data));
                    } else {
                        UNIT_TEST.assert(false);
                    }
                });
            }

            ,
            "getCurrentLocation": function () {
                uexGaodeMap.getCurrentLocation(function (error, data) {
                    UNIT_TEST.log("[getCurrentLocation data]" + JSON.stringify(data));
                    if (!error) {
                        UNIT_TEST.assert(true);
                    } else {
                        UNIT_TEST.assert(false);
                    }
                });
            }

            ,
            "startLocation": function () {
                var jsonstr = {
                    minTime: 3000,
                    minDistance: 10
                };
                var json = JSON.stringify(jsonstr);
                uexGaodeMap.startLocation(json);
                UNIT_TEST.assert(true);
            }

            ,
            "stopLocation": function () {
                uexGaodeMap.stopLocation();
                UNIT_TEST.assert(true);
            }

            ,
            "setMyLocationEnable": function () {
                var params = {
                    type: 1
                }
                var json = JSON.stringify(params);
                uexGaodeMap.setMyLocationEnable(json);
                UNIT_TEST.assert(true);
            }

            ,
            "setUserTrackingMode": function () {
                var params = {
                    type: 1
                }
                var json = JSON.stringify(params);
                uexGaodeMap.setUserTrackingMode(json);
                UNIT_TEST.assert(true);
            }

            ,
            "setScaleVisible": function () {
                var params = {
                    visible: true
                }
                var json = JSON.stringify(params);
                uexGaodeMap.setScaleVisible(json);
                UNIT_TEST.assert(true);
            }

            ,
            "setMyLocationButtonVisible": function () {
                if (uexWidgetOne.platformName.indexOf('android') > -1) {
                    var params = {
                        visible: true
                    }
                    var json = JSON.stringify(params);
                    uexGaodeMap.setMyLocationButtonVisible(json);
                    UNIT_TEST.assertDelay(true);
                } else {
                    UNIT_TEST.log("setMyLocationButtonVisible仅支持安卓，iOS请忽略!");
                    UNIT_TEST.assert(true);
                }
            }

            ,
            "setZoomVisible": function () {
                if (uexWidgetOne.platformName.indexOf('android') > -1) {
                    var params = {
                        visible: true
                    }
                    var json = JSON.stringify(params);
                    uexGaodeMap.setZoomVisible(json);
                    UNIT_TEST.assertDelay(true);
                } else {
                    UNIT_TEST.log("setZoomVisible仅支持安卓，iOS请忽略!");
                    UNIT_TEST.assert(true);
                }
            }

            ,

            "setCustomButton": function () {
                var param = {
                    id: 10010,
                    bgImage: "res://button2.png",
                    title: "title",
                    titleColor: "#F00",
                    x: 50,
                    y: 50,
                    width: 35,
                    height: 16
                }
                var buttonInfo = JSON.stringify(param)
                var button = uexGaodeMap.setCustomButton(buttonInfo);
                if (button) {
                    UNIT_TEST.assert(true);
                } else {
                    UNIT_TEST.assert(false);
                }
            }

            ,

            "showCustomButtons": function () {
                var buttons = '[10010]';
                var data = uexGaodeMap.showCustomButtons(buttons);
                if (data) {
                    UNIT_TEST.assert(true);
                } else {
                    UNIT_TEST.assert(false);
                }
            }

            ,

            "onMarkerClickListener": function () {
                UNIT_TEST.log("请点击标注...");
                uexGaodeMap.onMarkerClickListener = function (data) {
                    UNIT_TEST.log("[onMarkerClickListener data]" + data);
                    if (data) {
                        UNIT_TEST.assert(true);
                    } else {
                        UNIT_TEST.assert(false);
                    }
                }
            }

            ,
            "onMarkerBubbleClickListener": function () {
                if (uexWidgetOne.platformName.indexOf('android') > -1) {
                    UNIT_TEST.log("请点击气泡...");
                    uexGaodeMap.onMarkerBubbleClickListener = function (data) {
                        UNIT_TEST.log("[onMarkerBubbleClickListener data]" + data);
                        if (data) {
                            UNIT_TEST.assert(true);
                        } else {
                            UNIT_TEST.assert(false);
                        }
                    }
                } else {
                    UNIT_TEST.log("onMarkerBubbleClickListener仅支持安卓，iOS请忽略!");
                    UNIT_TEST.assert(true);
                }
            }

            ,

            "onMapClickListener": function () {
                UNIT_TEST.log("请点击地图...");
                uexGaodeMap.onMapClickListener = function (data) {
                    UNIT_TEST.log("[onMapClickListener data]" + data);
                    if (data) {
                        UNIT_TEST.assert(true);
                    } else {
                        UNIT_TEST.assert(false);
                    }
                }
            }

            ,
            "onMapLongClickListener": function () {
                UNIT_TEST.log("请长按地图...");
                uexGaodeMap.onMapLongClickListener = function (data) {
                    UNIT_TEST.log("[onMapLongClickListener data]" + JSON.stringify(data));
                    if (data) {
                        UNIT_TEST.assert(true);
                    } else {
                        UNIT_TEST.assert(false);
                    }
                    UNIT_TEST.log("[onMapLongClickListener data]" + data);
                }
            }

            ,
            "onCustomButtonClick": function () {
                UNIT_TEST.log("请点击自定义按钮...");
                uexGaodeMap.onCustomButtonClick = function (id) {
                    UNIT_TEST.log("[onCustomButtonClick id]" + id);
                    if (id) {
                        UNIT_TEST.assert(true);
                    } else {
                        UNIT_TEST.assert(false);
                    }
                    UNIT_TEST.log("[onCustomButtonClick id]" + data);
                }
            }

            ,
            "hideCustomButtons": function () {
                var buttons = '[10010]';
                var data = uexGaodeMap.hideCustomButtons(buttons);
                if (data) {
                    UNIT_TEST.assert(true);
                } else {
                    UNIT_TEST.assert(false);
                }
            }

            ,
            "deleteCustomButton": function () {
                var param = {
                    id: 10011,
                    bgImage: "res://button1.png",
                    title: "title",
                    titleColor: "#F00",
                    x: 50,
                    y: 50,
                    width: 35,
                    height: 16
                }
                var buttonInfo = JSON.stringify(param)
                var button = uexGaodeMap.setCustomButton(buttonInfo);
                var result = uexGaodeMap.deleteCustomButton(button);
                UNIT_TEST.assert(result);
            }

            ,
            "clear": function () {
                uexGaodeMap.clear();
                UNIT_TEST.assertDelay(true);
            }

            ,
            "close": function () {
                uexGaodeMap.close();
                UNIT_TEST.assertDelay(true);
            }

            ,

            "getAvailableCityList": function () {
                uexGaodeMap.delete();
                uexGaodeMap.getAvailableCityList(function (error, data) {
                    if (!error) {
                        cityName = data[0].city;
                        UNIT_TEST.assert(true);
                    } else {
                        UNIT_TEST.assert(false);
                    }
                });
            }

            ,
            "getAvailableProvinceList": function () {
                uexGaodeMap.getAvailableProvinceList(function (error, data) {
                    if (!error) {
                        UNIT_TEST.assert(true);
                    } else {
                        UNIT_TEST.assert(false);
                    }
                    //UNIT_TEST.log("[getAvailableProvinceList data]" + JSON.stringify(data));
                });
            }

            ,

            "download": function () {
                var params = [
                    {
                        city: cityName
                    }
                ];
                var json = JSON.stringify(params);
                uexGaodeMap.onDownload = function (info) {
                    var data = JSON.parse(info);
                    if (data.status == 0) {
                        UNIT_TEST.log("[onDownload status]正在下载...");
                    }
                    if (data.status == 1) {
                        UNIT_TEST.log("[onDownload status]正在解压...");
                    }
                    if (data.status == 4) {
                        isDownloaded = true;
                        UNIT_TEST.log("[onDownload status]离线地图下载成功...");
                        UNIT_TEST.assert(true);
                    }
                    if (data.status == 3) {
                        UNIT_TEST.log("[onDownload status]暂停下载...");
                    }
                    if (data.status == -1) {
                        isDownloaded = false;
                        UNIT_TEST.log(data.name + " 下载失败!");
                        UNIT_TEST.assert(false);
                    }
                }
                uexGaodeMap.download(json, downloadCallback);
                function downloadCallback(error, data) {
                    if (!error) {
                        UNIT_TEST.log("添加成功，请等待下载结果......");
                    } else {
                        UNIT_TEST.assert(false);
                    }
                };

            }

            ,
            "getDownloadingList": function () {
                uexGaodeMap.getDownloadingList(function (error, data) {
                    UNIT_TEST.log("[getDownloadingList data]" + data.length);
                    if (!error) {
                        UNIT_TEST.assert(true);
                    } else {
                        UNIT_TEST.assert(false);
                    }
                });
            }

            ,
            /*"pause": function(){
             var params = [cityName];
             var data = JSON.stringify(params);
             uexGaodeMap.pause(data);
             UNIT_TEST.assert(true);
             },
             "restart": function(){
             var params = [cityName];
             var data = JSON.stringify(params);
             uexGaodeMap.restart(data);
             UNIT_TEST.assert(true);
             },*/
            "getDownloadList": function () {
                uexGaodeMap.getDownloadList(function (error, data) {
                    if (!error) {
                        UNIT_TEST.assert(true);
                    } else {
                        UNIT_TEST.assert(false);
                    }
                    UNIT_TEST.log("[getDownloadList data]" + data.length);
                });
            }

            ,

            "isUpdate": function () {
                var params = {
                    city: cityName  //该市需要是地图已经下载过的，在本地有的
                };
                var json = JSON.stringify(params);
                if (isDownloaded) {
                    uexGaodeMap.isUpdate(json, function (error, data) {
                        UNIT_TEST.log("[isUpdate data]" + JSON.stringify(data));
                        if (!error) {
                            UNIT_TEST.assert(true);
                        } else {
                            UNIT_TEST.assert(false);
                        }
                    });
                } else {
                    UNIT_TEST.log("该市地图未下载过，没有更新信息");
                    UNIT_TEST.assert(false);
                }

            }

            ,

            "delete": function () {
                var params = [cityName];
                var json = JSON.stringify(params);
                var isDeleteExecute = false;
                if (uexWidgetOne.platformName.indexOf('android') > -1) {
                    uexGaodeMap.delete(json, function (error, data) {
                        UNIT_TEST.log("[delete]: " + JSON.stringify(data));
                        if (!isDeleteExecute) {
                            isDeleteExecute = true;
                            if (!error) {
                                UNIT_TEST.assert(true);
                            } else {
                                if (data.errorCode == 1) { //未下载，不能删除
                                    UNIT_TEST.assert(true);
                                } else {
                                    UNIT_TEST.assert(false);
                                }

                            }
                        }

                    });
                } else {
                    uexGaodeMap.delete(json);
                    UNIT_TEST.assert(true);
                }
            },

            /*
             "onReceiveLocation": function(){  //位置变化时才会被调用
             uexGaodeMap.onReceiveLocation = function(data) {
             if (!data) {
             UNIT_TEST.assert(true);
             } else {
             UNIT_TEST.assert(false);
             }
             UNIT_TEST.log("[onReceiveLocation data]" + data);
             }
             }
             */
            "queryProcessedTrace": function () {
                var params = {
                    sequenceLineId:1,
                    traceList:[
                        {"longitude":116.47676009385665,"time":1470212510269,"address":"","speed":10.471948623657227,"bearing":44.10205841064453,"provider":"gps","accuracy":350,"latitude":39.995825200614696},
                        {"longitude":116.4779446327818,"time":1470212512259,"address":"","speed":8.426321029663086,"bearing":32.86181640625,"provider":"gps","accuracy":16,"latitude":39.99609261805374},
                        {"longitude":116.47809869435011,"time":1470212514259,"address":"","speed":10.110260963439941,"bearing":41.294612884521484,"provider":"gps","accuracy":12,"latitude":39.996230255943914},
                        {"longitude":116.47834332726374,"time":1470212517000,"address":"","speed":9.825716972351074,"bearing":39.0571174621582,"provider":"gps","accuracy":12,"latitude":39.99640754897781},
                        {"longitude":116.47848735683651,"time":1470212520000,"address":"","speed":5.457311630249023,"bearing":32.60914993286133,"provider":"gps","accuracy":16,"latitude":39.99658892117307},
                        {"longitude":116.47846673090743,"time":1470212523000,"address":"","speed":6.840762615203857,"bearing":30.864465713500977,"provider":"gps","accuracy":24,"latitude":39.996708577271406},
                        {"longitude":116.47867796293544,"time":1470212526000,"address":"","speed":4.876165390014648,"bearing":18.661848068237305,"provider":"gps","accuracy":24,"latitude":39.99700372751301},
                        {"longitude":116.47881278947463,"time":1470212529000,"address":"","speed":9.283588409423828,"bearing":21.883132934570313,"provider":"gps","accuracy":24,"latitude":39.99714223899136},
                        {"longitude":116.47892120104655,"time":1470212532000,"address":"","speed":9.645008087158203,"bearing":18.514925003051758,"provider":"gps","accuracy":16,"latitude":39.997427648837096},
                        {"longitude":116.47900377104531,"time":1470212534000,"address":"","speed":10.45917797088623,"bearing":16.607942581176758,"provider":"gps","accuracy":16,"latitude":39.997624618553864},
                    ],
                    coordinateType:1
                };
                uexGaodeMap.queryProcessedTrace(params,function (error, data) {
                    console.log(JSON.stringify(data));
                    UNIT_TEST.assert(!error);
                });
            }
        }
        ;
    UNIT_TEST.addCase("uexGaodeMap", uexGaodeMapCase);
}
