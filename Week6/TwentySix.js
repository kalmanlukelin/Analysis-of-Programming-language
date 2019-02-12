//Get the string of all words.
let all_words=[undefined, null];

//Get the set of all stop words.
let stop_words=[undefined, null];

//Get the list of all non-stop words
let non_stop_words=[undefined, function func(){
    let res=[];
    //Get the index.
    for(let i in all_words[0]){
        if(!stop_words[0].has(all_words[0][i])){
            res.push(all_words[0][i]);
        }
    }
    return res;
}];

//Get the set of all words in file except for the stop words.
let unique_words=[undefined, function func(){return new Set(non_stop_words[0]);}];

//Get the frequency of each words in hash map.
let count=[undefined, function func(){
    let words_set=unique_words[0];
    let words_list=non_stop_words[0];
    let HashMap= {};
    for(let item of words_set){
        HashMap[item]=words_list.filter(function(x){return x == item}).length
    }
    return HashMap;
}];

//Get the list of map that is sorted by the frequency of characters.
let sorted_data=[undefined, function func(){
    var resultHash = {};
    var inputHash=count[0];
    var sortable = [];
    for (var item in inputHash) {
        sortable.push([item, inputHash[item]]);
    }
    sortable.sort(function(a, b) {
        return b[1] - a[1];
    });
    return sortable;
}];

let all_columns = [all_words, stop_words, non_stop_words, unique_words, count, sorted_data];

all_words[0]=require('fs').readFileSync(process.argv[2],'utf8').replace(/[^a-z0-9]/gmi, " ").toLowerCase().split(" ").filter(function(e) { return e !== ''&& e !== 's'}); //list
stop_words[0]=new Set((require('fs').readFileSync("../stop_words.txt",'utf8')).split(",")); //Set

function update(){
    for(let c in all_columns){
        if(all_columns[c][1] != null){
            all_columns[c][0]=all_columns[c][1]();
        }
    }
} 

update();

let output=sorted_data[0];
for(let i=0; i<25; i++){
    console.log(output[i][0]+" - "+output[i][1]);
}
