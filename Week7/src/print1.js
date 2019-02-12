module.exports = {
  output: function (word_freqs) {
    for(let e of word_freqs){
        console.log(e[0]+" - "+e[1]);
    }
  }
};