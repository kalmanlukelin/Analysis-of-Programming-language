module.exports = {
  top25: function (words) {
    let HashMap={};
    for(let w of words){
        if(!(w in HashMap)){
            HashMap[w]=0;
        }
        HashMap[w]=HashMap[w]+1;
    }
    
    var sortable = [];
    for (var item in HashMap) {
        sortable.push([item, HashMap[item]]);
    }
    sortable.sort(function(a, b) {
        return b[1] - a[1];
    });
    return sortable.slice(0, 25);
  }
};