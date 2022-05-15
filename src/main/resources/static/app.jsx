import Player from 'qier-player';

const player = new Player({
  src: 'https://trickle-media.sfo3.digitaloceanspaces.com/fixed-trimmed-end-sample.mp4?X-Amz-Algorithm=AWS4-HMAC-SHA256&X-Amz-Credential=HFR3ZTLLWMR43DB54UYV%2F20220515%2Fsfo3%2Fs3%2Faws4_request&X-Amz-Date=20220515T211731Z&X-Amz-Expires=604800&X-Amz-SignedHeaders=host&X-Amz-Signature=2ac1ac902a485cfa80ffbb08d9fa275fab58ebfa595a6680eccec5e04405d5fe'
});
player.mount('#app');