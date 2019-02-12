let tfwords;
let tffreqs;
let tfprint;

function load_plugins(){
    let conf = require('./config.json');
    tfwords=require(conf.words);
    tffreqs=require(conf.frequency);
    tfprint=require(conf.print);
}

load_plugins()

let word_freqs=tffreqs.top25(tfwords.extract_words(process.argv[2]));
tfprint.output(word_freqs);