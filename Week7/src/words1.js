module.exports = {
  extract_words: function (path) {
    let f=require('fs').readFileSync(path,'utf8').replace(/[^a-z0-9]/gmi, " ");
    let word_list=f.toLowerCase().split(" ").filter(function(e) { return e !== ''&& e !== 's'});
    let stop_words=new Set((require('fs').readFileSync("../stop_words.txt",'utf8')).split(","));
    return word_list.filter(function(e) {return !stop_words.has(e)});
  }
};