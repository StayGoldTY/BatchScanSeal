<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width,minimum-scale=1.0,maximum-scale=1.0,user-scalable=no"/>
    <title>批量盖章</title>
    <link rel="shortcut icon" href="favicon.ico" type="image/x-icon"/>
    <link href="static/bootstrap/bootstrap.min.css" rel="stylesheet"/>
    <link href="static/bootstrap-table/bootstrap-table.min.css" rel="stylesheet"/>

    <script src="static/jquery.min.js"></script>
    <script src="static/bootstrap/bootstrap.min.js"></script>
    <script src="static/bootstrap-table/bootstrap-table.min.js"></script>
    <script src="static/bootstrap-table/bootstrap-table-export.min.js"></script>
    <script src="static/bootstrap-table/bootstrap-table-zh-CN.min.js"></script>
    <script src="https://szhn-onecode-bucket.oss-cn-shenzhen.aliyuncs.com/js/onecodeN-v1.2.2.js"
            type="text/javascript" charset="utf-8"></script>
    <script type="text/javascript">
        window.operateEvents = {
            'click #like': function (e, value, row, index) {
                alert('You click like icon, row: ' + JSON.stringify(row));
                console.log(value, row, index);
            },
            'click #edit': function (e, value, row, index) {
                console.log(value)
                console.log('You click remove icon, row: ' + JSON.stringify(row));
                window.open("pdf/" + row.name, "_blank");
            },
            'click #remove': function (e, value, row, index) {
                console.log('You click remove icon, row: ' + JSON.stringify(row));
                window.open("pdf/stamp/" + row.stampName, "_blank");
            }
        };
    </script>
</head>
<body>
<!-- 表示一个成功的或积极的动作 -->
<button id="fat-btn" type="button" class="btn btn-success" data-loading-text="Loading...">批量盖企业章</button>
<button id="fat-btn-2" type="button" class="btn btn-warning" data-loading-text="Loading...">批量盖个人章</button>

<button id="fat-btn-3" type="button" class="btn btn-danger" data-loading-text="Loading...">会话模式个人章</button>
<button id="fat-btn-4" type="button" class="btn btn-default" data-loading-text="Loading...">会话模式企业章</button>

<table id="dtb" data-toggle="table"
       data-url="getFile"
       class="table table-hover"
       data-page-size="50"
       data-height="450"
       data-side-pagination="server"
       data-page-list="[5, 10, 20, 50, 100, 200]"
       data-query-params="">
    <thead>
    <tr>
        <th data-field="fileId" data-formatter="hideIdFormatter" class="hide"></th>
        <th data-field="name">文件名</th>
        <th data-field="operation" data-formatter="operateFormatter"
            data-events="operateEvents">操作
        </th>
    </tr>
    </thead>
</table>

<div class="modal fade" id="myModal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel">
    <div class="modal-dialog" role="document">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span>
                </button>
                <h4 class="modal-title" id="myModalLabel">二维码</h4>
            </div>
            <div class="modal-body" style="text-align: center;">
                <img src="" class="img-responsive center-block" id="img" alt="Cinque Terre">
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-default" data-dismiss="modal">
                    <span class="glyphicon glyphicon-remove" aria-hidden="true"></span>关闭
                </button>
                <%--<button type="button" id="btn_submit" class="btn btn-primary" data-dismiss="modal">
                    <span class="glyphicon glyphicon-floppy-disk" aria-hidden="true"></span>保存
                </button>--%>
            </div>
        </div>
    </div>
</div>
</body>

<script type="text/javascript">
    $(function () {
        $("#fat-btn").click(function () {
            $(this).button('loading').delay(1000).queue(function () {
                // 请求二维码
                const data = {"way": "1", "type": "2"};
                postJSONDomain("scanQrcode", data, function (res) {
                    if (res.code == "0") {
                        // 显示二维码
                        $("#img").attr("src", "data:image/png;base64," + res.data.image)
                        $('#myModal').modal('show');
                        // 请求轮询盖章
                        const batchScanSealParam = {"qrcodeId": res.data.qrcodeId, "sessionMode": "0"};
                        postJSONDomain("batchScanSeal", batchScanSealParam, function (res1) {
                            if (res1.code == "0") {
                                // 关闭二维码
                                $('#myModal').modal('hide');
                                alert(res1.message);
                            } else {
                                alert("批量盖章异常请重新扫码：" + res1.message);
                            }
                        })
                    } else {
                        alert("获取二维码异常：" + res.message);
                    }
                })
                /*$.ajax({
                    type: "post",
                    url: "scanQrcode",
                    data: "war=1&type=2", // 此处data可以为 a=1&b=2类型的字符串 或 json数据。
                    // data: {"way": "1", "type": "2"},
                    cache: false,
                    async : false,
                    dataType: "json",
                    success: function (data ,textStatus, jqXHR)
                    {
                        if("0" == data.code || 0 == data.code){
                            alert("合法！");
                            return true;
                        } else {
                            alert("请求二维码失败：" + data.message);
                            return false;
                        }
                    },
                    error:function (XMLHttpRequest, textStatus, errorThrown) {
                        alert("请求二维码失败！" + errorThrown);
                    }
                });*/
                $(this).button('reset');
                $(this).dequeue();
            });
        });

        // 批量盖个人章
        $("#fat-btn-2").click(function () {
            $(this).button('loading').delay(1000).queue(function () {
                // 请求二维码
                const data = {"way": "1", "type": "1"};
                postJSONDomain("scanQrcode", data, function (res) {
                    if (res.code == "0") {
                        // 显示二维码
                        $("#img").attr("src", "data:image/png;base64," + res.data.image)
                        $('#myModal').modal('show');
                        // 请求轮询盖章
                        const batchScanSealParam = {"qrcodeId": res.data.qrcodeId, "sessionMode": "0"};
                        postJSONDomain("batchScanSeal", batchScanSealParam, function (res1) {
                            if (res1.code == "0") {
                                // 关闭二维码
                                $('#myModal').modal('hide');
                                alert(res1.message);
                            } else {
                                alert("批量盖章异常请重新扫码：" + res1.message);
                            }
                        })
                    } else {
                        alert("获取二维码异常：" + res.message);
                    }
                })
                $(this).button('reset');
                $(this).dequeue();
            });
        });

    });

    // 会话模式盖个人章
    $("#fat-btn-3").click(function () {
        $(this).button('loading').delay(1000).queue(function () {
            // 请求二维码
            const data = {"way": "1", "type": "1"};
            postJSONDomain("scanQrcode", data, function (res) {
                if (res.code == "0") {
                    // 显示二维码
                    $("#img").attr("src", "data:image/png;base64," + res.data.image)
                    $('#myModal').modal('show');
                    // 请求轮询盖章
                    const batchScanSealParam = {"qrcodeId": res.data.qrcodeId, "sessionMode": "1", "category": "1"};
                    postJSONDomain("batchScanSeal", batchScanSealParam, function (res1) {
                        if (res1.code == "0") {
                            // 关闭二维码
                            $('#myModal').modal('hide');
                            alert(res1.message);
                        } else {
                            alert("批量盖章异常：" + res1.message);
                        }
                    })
                } else {
                    alert("获取二维码异常：" + res.message);
                }
            })
            $(this).button('reset');
            $(this).dequeue();
        }).catch(reason => {
            alert("获取建立会话授权凭证：" + reason);
        })
        $(this).button('reset');
        $(this).dequeue();
    });

    // 会话模式盖企业章
    $("#fat-btn-4").click(function () {
        $(this).button('loading').delay(1000).queue(function () {
            // 请求二维码
            const data = {"way": "1", "type": "2"};
            postJSONDomain("scanQrcode", data, function (res) {
                if (res.code == "0") {
                    // 显示二维码
                    $("#img").attr("src", "data:image/png;base64," + res.data.image)
                    $('#myModal').modal('show');
                    // 请求轮询盖章
                    const batchScanSealParam = {"qrcodeId": res.data.qrcodeId, "sessionMode": "1", "category": "2"};
                    postJSONDomain("batchScanSeal", batchScanSealParam, function (res1) {
                        if (res1.code == "0") {
                            // 关闭二维码
                            $('#myModal').modal('hide');
                            alert(res1.message);
                        } else {
                            alert("批量盖章异常：" + res1.message);
                        }
                    })
                } else {
                    alert("获取二维码异常：" + res.message);
                }
            })
            $(this).button('reset');
            $(this).dequeue();
        }).catch(reason => {
            alert("获取建立会话授权凭证：" + reason);
        })
        $(this).button('reset');
        $(this).dequeue();
    });

    function operateFormatter(value, row, index) {
        return [
            '<a id="edit" style="color:black;" class="btn btn-info" data-toggle="modal" data-target="#editModal" href="javascript:void(0)">',
            '原文件',
            '</a>&emsp;',
            '<a id="remove" style="color:black;" class="btn btn-danger" style="margin-left:10px;">', '盖章文件', , '</a>'].join('');
    }

    function hideIdFormatter(value, row, index) {
        return "<input type='hidden' value=" + value + " name='fileId' />";
    }

    /**
     * 用户授权，获取 authCode
     */
    function getAuthCode() {
        return new Promise((resolve, reject) => {
            if (window.OnecodeJSBridge) {
                window.OnecodeJSBridge.call('getAuthCode', {}, response => {
                    /**
                     * response 数据说明：
                     *     status：获取是否成功
                     *     errorMsg：失败的原因
                     *     authCode：授权码
                     */
                    if (response.status) {
                        resolve(response.authCode)
                    } else {
                        reject(response.errorMsg)
                    }
                })
            } else {
                reject('OnecodeJSBridge初始化失败, 请在码上办事小程序或者是APP中打开')
            }
        })
    }

    /**
     * 接口请求 本域
     * @param url
     * @param data
     * @param callback
     * @returns {*}
     */
    function postJSONDomain(url, data, callback) {

        // var index_load = layer.load();
        //var index_load = layer.load(2);
        if ($.isFunction(data)) {
            callback = data;
            data = undefined;
        }
        return $.ajax({
            url: url,
            type: "POST",
            data: data,
            success: function (res) {
                callback(res);
            },
            dataType: "json",
            error: function (jqXHR, textStatus, errorMsg) {
                alert("请求异常：" + errorMsg);
            }
        });
    }
</script>
</html>
