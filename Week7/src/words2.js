module.exports = {
  extract_words: function (path) {
    let words=require('fs').readFileSync(path,'utf8').replace(/[^a-z0-9]/gmi, " ").toLowerCase().split(" ").filter(function(e) { return e !== ''&& e !== 's'}); //list
    let stop_words=new Set((require('fs').readFileSync("../stop_words.txt",'utf8')).split(","));
    return words.filter(function(e) {return !stop_words.has(e)});
  }
};