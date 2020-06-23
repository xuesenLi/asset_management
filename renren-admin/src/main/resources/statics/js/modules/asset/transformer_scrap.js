$(function () {

    var recordNo = T.p("recordNo");
    //报废 单号
    vm.init(recordNo);

    $("#jqGrid").jqGrid({
        url: baseURL + 'asset/asset/listByRecordNo/' + recordNo,
        datatype: "json",
        postData: {
            recordNo: recordNo === null ? "" : recordNo
        },
        colModel: [
            { label: '资产状态', name: 'assetStatus', index: 'asset_status', width: 60, formatter: function(value, options, row){
                    if(value === 0)
                        return '<span class="label label-info">闲置</span>';
                    else if(value === 1)
                        return '<span class="label label-success">在用</span>';
                    else if(value === 2)
                        return '<span class="label label-primary">借用</span>';
                    else if(value === 3)
                        return '<span class="label label-danger">维修中</span>';
                    else if(value === 4)
                        return '<span class="label label-default">报废</span>';
                    else if(value === 5)
                        return '<span class="label label-warning">待审批</span>';

                } },
            { label: 'id', name: 'id', index: 'id', width: 50, key: true, hidden: true },
            { label: '资产编码', name: 'assetCode', index: 'asset_code', width: 100 },
            { label: '资产名称', name: 'assetName', index: 'asset_name', width: 80 },
            { label: '资产分类名称', name: 'categoryName', index: 'category_name', width: 80 },
            { label: '所属组织名称', name: 'orgName', index: 'org_name', width: 80 },
            { label: '使用组织名称', name: 'useOrgName', index: 'use_org_name', width: 80 },
            { label: '使用人名称', name: 'empName', index: 'emp_name', width: 80 },
            { label: '管理员名称', name: 'adminUsername', index: 'admin_username', width: 80 },
            { label: '区域名称', name: 'areaName', index: 'area_name', width: 80 },
            { label: '购买日期', name: 'buyTime', index: 'buy_time', width: 80 }
        ],
        viewrecords: true,
        height: 385,
        rowNum: 10,
        rowList: [10, 30, 50],
        rownumbers: true,
        rownumWidth: 40,
        autowidth: true,
        multiselect: true,
        pager: "#jqGridPager",
        jsonReader: {
            root: "page.list",
            page: "page.currPage",
            total: "page.totalPage",
            records: "page.totalCount"
        },
        prmNames: {
            page: "page",
            rows: "limit",
            order: "order"
        },
        gridComplete: function () {
            //隐藏grid底部滚动条
            $("#jqGrid").closest(".ui-jqgrid-bdiv").css({"overflow-x": "hidden"});
        }
    });

});

var vm = new Vue({
    el:'#rrapp',
    data:{
        showList: true,
        title: null,
        assetScrap:{},
        recordNo: null,
        //是否显示 操作、 驳回、 确定按钮
        isBtn: false
    },

    methods: {

        init: function(recordNo){
            vm.recordNo = recordNo;
            $.get(baseURL + "asset/assetscrap/infoByRecordNo/"+ recordNo, function(r){
                vm.assetScrap = r.assetScrap;
                if(r.assetScrap.recordStatus === 0){
                    // 表示为待审批
                    vm.isBtn = true;
                }
            });
        },

        /** 同意 */
        save: function(){
            $.ajax({
                type: "POST",
                url: baseURL + "asset/assetscrap/agree",
                data: "recordNo=" + vm.recordNo,
                success: function(r){
                    if(r.code === 0){
                        alert('操作成功', function(){
                            vm.reload();
                            //刷新页面
                            window.location.reload();
                        });
                    }else{
                        alert(r.msg);
                    }
                }
            });
        },

        /** 驳回 */
        rebut: function(){
            $.ajax({
                type: "POST",
                url: baseURL + "asset/assetscrap/rebut",
                data: "recordNo=" + vm.recordNo,
                success: function(r){
                    if(r.code === 0){
                        alert('操作成功', function(){
                            vm.reload();
                            //刷新页面
                            window.location.reload();
                        });
                    }else{
                        alert(r.msg);
                    }
                }
            });
        },


        query: function () {
            vm.reload();
        },

        reload: function (event) {
            vm.showList = true;
            var page = $("#jqGrid").jqGrid('getGridParam','page');
            $("#jqGrid").jqGrid('setGridParam',{
                page:page
            }).trigger("reloadGrid");
        }
    }
});
