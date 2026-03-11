import { KeyValuePair } from "./pfModel";

export class PfUtil {
  constructor() {}

  setUrlParams(url: string, arr: KeyValuePair[] | Object): string {
    if (arr == null || arr == undefined) {
      return url;
    }
    if (url.indexOf("?") < 0) {
      url += "?";
    } else {
      var lc = url[url.length - 1];
      if (lc !== "?" && lc !== "&") {
        url += "&";
      }
    }

    function setParam(sUrl, name, val) {
      if (val instanceof Array) {
        for (var i = 0; i < val.length; i++) {
          sUrl += name + "=" + encodeURIComponent(val[i]) + "&";
        }
      } else {
        sUrl += name + "=" + encodeURIComponent(val) + "&";
      }
      return sUrl;
    }
    function removeSameParam(sUrl, pName) {
      //移除url中已经存在的同名参数,已考虑 xx[]的情况
      var patt1 = new RegExp(
        "([&?]{1})" + pName + "[\\[\\]]{0,2}=[^&]*[&]{0,1}",
        "g"
      );
      return sUrl.replace(patt1, "$1");
    }
    //到这里url格式为 xx? 或者 xx?xx&
    if (arr instanceof Array) {
      for (var i = 0; i < arr.length; i++) {
        //考虑到有数组的情况,必需全部移除再设置
        url = removeSameParam(url, arr[i].key);
      }
      for (var i = 0; i < arr.length; i++) {
        url = setParam(url, arr[i].key, arr[i].value);
      }
    } else {
      //object
      for (var item in arr) {
        //考虑到有数组的情况,必需全部移除再设置
        if (arr.hasOwnProperty(item)) {
          url = removeSameParam(url, item);
        }
      }
      for (var item in arr) {
        if (arr.hasOwnProperty(item)) {
          url = setParam(url, item, arr[item]);
        }
      }
    }
    if (url[url.length - 1] === "&") {
      url = url.substr(0, url.length - 1);
    }
    return url;
  }
}

interface ProcedureLink<T> {
  source: T;
  target: T;
}
/*
 * 用于步骤的增删改 未测试
 * node格式如：
 * {
 * 	procedureId
 *  procedureName
 *  from:[]
 *  to:[]
 * }
 * 使用方法：
 * 
            let procedureManager = new ProcedureManager(
              "procedureId","from","to"
            );
 */
export class ProcedureManager<T> {
  nodes: T[] = []; //格式如{procedureId,from:[],to:[]}
  links: ProcedureLink<T>[] = []; //格式如[{ source: node, target: node }]
  constructor(
    protected key: string,
    protected from: string,
    protected to: string
  ) {}
  private getNode(v) {
    var me = this;
    for (var i = 0; i < me.nodes.length; i++) {
      if (v == me.nodes[i][me.key]) {
        return me.nodes[i];
      }
    }
    return null;
  }
  public addProcedure = function (node) {
    var me = this;
    me.nodes.push(node);
    for (var i = 0; i < node[me.from].length; i++) {
      var n = me.getNode(node[me.from][i]);
      if (n != null) {
        me.links.push({ source: n, target: node });

        if (n.to.findIndex((a) => a == node[me.key]) < 0) {
          //新加节点时,是为true进入这里的,但由于edit页面加载时也是用addProcedure初始化数据库的数据,那么target里早就有关联了,所以不应再进入这里
          n.to.push(node[me.key]); //必要
        }
      }
    }
    for (var i = 0; i < node[me.to].length; i++) {
      var n = me.getNode(node[me.to][i]);
      if (n != null) {
        me.links.push({ source: node, target: n });

        if (n.from.findIndex((a) => a == node[me.key]) < 0) {
          n.from.push(node[me.key]);
        }
      }
    }
  };
  /**
   * 编辑时要保留node的原有位置
   */
  public editProcedure = function (node) {
    var me = this;
    var oldI = -1;
    for (var i = 0; i < me.nodes.length; i++) {
      if (node[me.key] == me.nodes[i][me.key]) {
        oldI = i;
      }
    }
    //删线
    for (var i = me.links.length - 1; i >= 0; i--) {
      if (
        me.nodes[oldI][me.key] == me.links[i].source[me.key] ||
        me.nodes[oldI][me.key] == me.links[i].target[me.key]
      ) {
        me.links.splice(i, 1);
      }
    }
    //删原node的相关from to
    for (var i = 0; i < me.nodes[oldI][me.from].length; i++) {
      var n = me.getNode(me.nodes[oldI][me.from][i]);
      n.to = n.to.filter((a) => a != me.nodes[oldI][me.key]);
    }
    for (var i = 0; i < me.nodes[oldI][me.to].length; i++) {
      var n = me.getNode(me.nodes[oldI][me.to][i]);
      n.from = n.from.filter((a) => a != me.nodes[oldI][me.key]);
    }
    //加线
    for (var i = 0; i < node[me.from].length; i++) {
      var n = me.getNode(node[me.from][i]);
      if (n != null) {
        me.links.push({ source: n, target: node });
      }
    }
    for (var i = 0; i < node[me.to].length; i++) {
      var n = me.getNode(node[me.to][i]);
      if (n != null) {
        me.links.push({ source: node, target: n });
      }
    }
    //加node的相关from to
    for (var i = 0; i < node[me.from].length; i++) {
      var n = me.getNode(node[me.from][i]);
      if (n != null) {
        n.to.push(node[me.key]); //必要
      }
    }
    for (var i = 0; i < node[me.to].length; i++) {
      var n = me.getNode(node[me.to][i]);
      if (n != null) {
        n.from.push(node[me.key]);
      }
    }
    //替换node
    me.nodes.splice(oldI, 1, node);
  };
  public deleteProcedure = function (node) {
    var me = this;
    for (var i = 0; i < me.nodes.length; i++) {
      if (node[me.key] == me.nodes[i][me.key]) {
        me.nodes.splice(i, 1);
        break;
      }
    }
    for (var i = me.links.length - 1; i >= 0; i--) {
      if (
        node[me.key] == me.links[i].source[me.key] ||
        node[me.key] == me.links[i].target[me.key]
      ) {
        me.links.splice(i, 1);
      }
    }
  };
  public getLinks(): ProcedureLink<T>[] {
    var me = this;
    return me.links;
  }
  public getNodes(): T[] {
    var me = this;
    return me.nodes;
  }
  public printContent() {
    var me = this;
    console.info("---------nodes--------");
    console.info(me.nodes);
    console.info("---------links--------");
    console.info(me.links);
  }
}
