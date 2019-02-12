module.exports = {
  output: function (word_freqs) {
    for(let i=0; i<word_freqs.length; i++){
      console.log(word_freqs[i][0]+" - "+word_freqs[i][1]);
    }
  }
};