function extract_words(path){
  let f=require('fs').readFileSync(path,'utf8').replace(/[^a-z0-9]/gmi, " ");
  let word_list=f.toLowerCase().split(" ").filter(function(e) { return e !== ''&& e !== 's'});
  let stop_words=new Set((require('fs').readFileSync("../stop_words.txt",'utf8')).split(","));
  return word_list.filter(function(e) {return !stop_words.has(e)});
}

function frequency(words){
  let HashMap={};
  for(let w of words){
    if(!(w in HashMap)){
      HashMap[w]=0;
    }
    HashMap[w]=HashMap[w]+1;
  }
  return HashMap;
}
  
function sort(HashMap){
  let sortable = [];
  for (var item in HashMap) {
    sortable.push([item, HashMap[item]]);
  }
  sortable.sort(function(a, b) {
    return b[1] - a[1];
  });
  return sortable;
}

function profile(func, input){
    var start = new Date().getTime();
    let output=func(input);
    var end = new Date().getTime();
    var dur = end - start;
    console.log(arguments[0]);
    console.log("took "+dur+" milliseconds ");
    return output
}

profile(sort, profile(frequency, profile(extract_words, process.argv[2])));

console.log();
let word_freqs=sort(frequency(extract_words(process.argv[2])));
for(let i=0; i<25; i++){
    console.log(word_freqs[i][0]+" - "+word_freqs[i][1]);
}