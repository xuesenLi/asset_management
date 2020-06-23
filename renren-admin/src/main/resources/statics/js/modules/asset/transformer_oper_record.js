$(function () {

    var assetId = T.p("assetId");

    $("#jqGrid").jqGrid({
        url: baseURL + 'asset/assetoperrecord/list/' + assetId,
        datatype: "json",
        postData: {
            recordNo: assetId === null ? "" : assetId
        },
        colModel: [
            { label: 'id', hidden:true, name: 'id', index: 'id', width: 50, key: true },
            //{ label: '资产id', name: 'assetId', index: 'asset_id', width: 80 },
            { label: '处理类型', name: 'operType', index: 'oper_type', width: 60, formatter: function(value, options, row){
                //* 0 领用、 1 退还、 2 借用、   3 归还 、 4 变更、  5 调拨 、 6 维修、  7 报废、  8 维修完成、 9 入库*/
                    if(value === 0)
                        return '领用';
                    else if(value === 1)
                        return '退还';
                    else if(value === 2)
                        return '借用';
                    else if(value === 3)
                        return '归还';
                    else if(value === 4)
                        return '变更';
                    else if(value === 5)
                        return '调拨';
                    else if(value === 6)
                        return '维修';
                    else if(value === 7)
                        return '报废';
                    else if(value === 8)
                        return '维修完成';
                    else if(value === 9)
                        return '入库';
                } },
            { label: '处理时间', name: 'operTime', index: 'oper_time', width: 80 },
            //{ label: '申请人id', name: 'createdUserid', index: 'created_userid', width: 80 },
            { label: '处理人', name: 'createdUsername', index: 'created_username', width: 80 },
            { label: '处理内容', name: 'operContent', index: 'oper_content', width: 120 },
            { label: '关联单号', name: 'recordNo', index: 'record_no', width: 120 }
        ],
        viewrecords: true,
        height: 385,
        rowNum: 10,
        rowList: [10, 30, 50],
        rownumbers: true,
        rownumWidth: 40,
        autowidth: true,
        multiselect: false,
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
        //显示盘点记录true  或则 处理记录 false
        showList: true,
        title: null,
        assetOperrecord:{

        },

    },

    methods: {

        /*showListBtn: function(){
            vm.showList = true;
        },

        unShowListBtn: function(){
            vm.showList = false;
        },*/

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
