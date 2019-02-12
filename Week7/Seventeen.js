//Read the whole file into string.
let s_read_file="f=function read_file(path){\
    return require('fs').readFileSync(path,'utf8');\
}"

//Repalce nonalphanumeric characters with white space. 
let s_filter_chars='f=function filter_chars(str_data){\
    return str_data.replace(/[^a-z0-9]/gmi, " ");\
}'


//Change all the characters into lowercase.
let s_normalize="f=function normalize(str_data){\
    return str_data.toLowerCase();\
}"

//Add all the words from the string to the list.
let s_scan='f=function scan(str_data){\
    return str_data.split(" ").filter(function(e) { return e !== ""&& e !== "s"});\
}'


let s_removeStopWords='f=function removeStopWords(words){\
    let stop_words=new Set((require("fs").readFileSync("../stop_words.txt","utf8")).split(","));\
    let non_stop_words=[];\
    for(let w of words){\
        if(!stop_words.has(w)){\
            non_stop_words.push(w);\
        }\
    }\
    return non_stop_words;\
}'

let s_frequency="f=function frequency(words){\
    let HashMap= {};\
    for(let w of words){\
        if(!(w in HashMap)){\
            HashMap[w]=0;\
        }\
        HashMap[w]=HashMap[w]+1;\
    }\
    return HashMap;\
}"

let s_sortByValue="f=function sortByValue(HashMap){\
    let sortable = [];\
    for (var item in HashMap) {\
        sortable.push([item, HashMap[item]]);\
    }\
    sortable.sort(function(a, b) {\
        return b[1] - a[1];\
    });\
    return sortable;\
}"

let read_file=eval(s_read_file);
let filter_chars=eval(s_filter_chars);
let normalize=eval(s_normalize);
let scan=eval(s_scan);
let removeStopWords=eval(s_removeStopWords);
let frequency=eval(s_frequency);
let sortByValue=eval(s_sortByValue);

let output=sortByValue(frequency(removeStopWords(scan(normalize(filter_chars(read_file(process.argv[2])))))))

for(let i=0; i<25; i++){
    console.log(output[i][0]+" - "+output[i][1]);
}