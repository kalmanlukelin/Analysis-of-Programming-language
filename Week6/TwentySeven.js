//Read line from the file.
function *lines(filename){
    let all_lines=require('fs').readFileSync(filename,'utf8').split("\n");
    for(let line of all_lines){
        yield line;
    }
}

//Get a word.
function *all_words(filename){
    for(let line of lines(filename)){
        let word_list=line.replace(/[^a-z0-9]/gmi, " ").toLowerCase().split(" ").filter(function(e) { return e !== ''&& e !== 's'});
        for(let word of word_list){
            yield word;
        }
    }
}

//Get non stop words.
function *non_stop_words(filename){
    let stopwords=new Set((require('fs').readFileSync("../stop_words.txt",'utf8')).split(","));
    for(let w of all_words(filename)){
        if(!stopwords.has(w) && w != "s"){
            yield w;
        }
    }
}

//Get the list of sorted frequency of the map.
function *count_and_sort(filename){
    let freqs={}, i=1;
    let sortable;
    for(let w of non_stop_words(filename)){
        if(!(w in freqs)){
            freqs[w]=1;
        }
        else{
            freqs[w]+=1;
        }
        if((i % 5000) == 0){
            sortable = [];
            for (let item in freqs) {
                sortable.push([item, freqs[item]]);
            }
            sortable.sort(function(a, b) {
                return b[1] - a[1];
            });
            yield sortable;
        }
        i+=1;
    }
    sortable = [];
    for (let item in freqs) {
        sortable.push([item, freqs[item]]);
    }
    sortable.sort(function(a, b) {
        return b[1] - a[1];
    });
    yield sortable;
}

for(let word_freqs of count_and_sort(process.argv[2])){
    console.log("--------------------------------");
    for(let i=0; i<25; i++){
        console.log(word_freqs[i][0]+" - "+word_freqs[i][1]);
    }
}