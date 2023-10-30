
<script setup>
import { ref, onMounted } from 'vue';
const play = ref(true)
const game = ref(false)
const end = ref(false)
const hasPosted = ref(false)
const name = ref("")
const score = ref(0)
const isScoreBoardVisible = ref(false);
const scoreBoardData = ref([]);
let counter = 60


let numArr = []
let color = [
  {
    "diffColor": "bg-gray-400",
    "sameColor": "bg-gray-500"
  },
  {
    "diffColor": "bg-red-400",
    "sameColor": "bg-red-500"
  },
  {
    "diffColor": "bg-orange-400",
    "sameColor": "bg-orange-500"
  },
  {
    "diffColor": "bg-amber-600",
    "sameColor": "bg-amber-500"
  },
  {
    "diffColor": "bg-yellow-900",
    "sameColor": "bg-yellow-800"
  },
  {
    "diffColor": "bg-lime-800",
    "sameColor": "bg-lime-700"
  },
  {
    "diffColor": "bg-green-500",
    "sameColor": "bg-green-600"
  },
  {
    "diffColor": "bg-emerald-500",
    "sameColor": "bg-emerald-400"
  },
  {
    "diffColor": "bg-teal-700",
    "sameColor": "bg-teal-800"
  },
  {
    "diffColor": "bg-cyan-800",
    "sameColor": "bg-cyan-900"
  },
  {
    "diffColor": "bg-sky-700",
    "sameColor": "bg-sky-800"
  },
  {
    "diffColor": "bg-blue-800",
    "sameColor": "bg-blue-900"
  },
  {
    "diffColor": "bg-indigo-600",
    "sameColor": "bg-indigo-700"
  },
  {
    "diffColor": "bg-violet-500",
    "sameColor": "bg-violet-600"
  },
  {
    "diffColor": "bg-purple-400",
    "sameColor": "bg-purple-500"
  },
  {
    "diffColor": "bg-fuchsia-700",
    "sameColor": "bg-fuchsia-800"
  },
  {
    "diffColor": "bg-pink-800",
    "sameColor": "bg-pink-900"
  },
  {
    "diffColor": "bg-rose-800",
    "sameColor": "bg-rose-900"
  }
]

const formatDate = (rawDate) => {
  const date = new Date(rawDate);
  
  const options = {
    timeZone: 'Asia/Bangkok',
    day: '2-digit',
    month: '2-digit',
    year: 'numeric'
  };

  const thaiDateFormatter = new Intl.DateTimeFormat(undefined, options);
  return thaiDateFormatter.format(date);
};

const comma = (num) => {
  return num.toLocaleString("en")
}

setInterval(() => {
  if (counter > 0) {
    counter--
  }
  else if (counter == 0) {
    endgame()
  }
  document.getElementById('counterElement').style.setProperty('--value', counter)
}, 1000)

const start = () => {
  counter = 61
  score.value = 0
  play.value = false
  game.value = true
  end.value = false
  hasPosted.value = false; 
}


const mainmenu = () => {
  counter = 61
  score.value = 0
  play.value = true
  game.value = false
  end.value = false
}

const endgame = () => {
  if (!hasPosted.value) {
    addPlayer(name.value, score.value)
      .then(() => {
        hasPosted.value = true;
        game.value = false;
        play.value = false;
        end.value = true;
        getAllPlayer()
          .then(data => {
            scoreBoardData.value = data;
          })
          .catch(error => {
            console.error('Error fetching player data:', error);
          });
      })
      .catch(error => {
        console.error('Error adding player:', error);
      });
  }
}



const restart = () => {
  counter = 61
  score.value = 0
  hasPosted.value = false;
}

const getRandomColor = () => {
  numArr = []
  let circle = 0;
  if (score.value > 5 && score.value <= 15) {
    circle = 9;
  }
  else if (score.value > 15 && score.value <= 25) {
    circle = 16;
  }
  else if (score.value > 25) {
    circle = 25;
  } else {
    circle = 4;
  }

  const randomColor = color[Math.floor(Math.random() * color.length)];
  for (let i = 0; i < circle; i++) {
    if (numArr.length < circle - 1) {
      numArr.push(randomColor.diffColor)
    }
    else {
      numArr.push(randomColor.sameColor)
      break;
    }
  }

  shuffleArray(numArr)
  return numArr;
}


const block = () => {
  if (score.value > 5 && score.value <= 15) {
    return 'grid-cols-3';
  }
  if (score.value > 15 && score.value <= 25) {
    return 'grid-cols-4';
  }
  if (score.value > 25) {
    return 'grid-cols-5';
  }
  return 'grid-cols-2';
}

function shuffleArray(array) {
  console.log(array)
  for (let i = array.length - 1; i > 0; i--) {
    const j = Math.floor(Math.random() * (i + 1));
    [array[i], array[j]] = [array[j], array[i]];
  }
}

let check = (answer) => {
  for (let i = 0; i < color.length; i++) {
    if (answer == color[i].sameColor) {
      score.value++
      getRandomColor()
      break;
    }
  }

}

const size = () => {
  if (score.value <= 5) {
    return 'w-36 h-36'
  }
  if (score.value > 5 && score.value <= 15) {
    return 'w-24 h-24'
  }
  if (score.value > 15 && score.value <= 25) {
    return 'w-20 h-20'
  }
  if (score.value > 25) {
    return 'w-16 h-16'
  }
}
getRandomColor();

onMounted(() => {
  if (scoreBoardData.value.length === 0) {
    getAllPlayer()
      .then(data => {
        scoreBoardData.value = data;
      })
      .catch(error => {
        console.error('Error fetching player data:', error);
      });
  }
});

const getAllPlayer = async () => {
  try {
    const url = 'http://localhost:8080/api/players';
    const res = await fetch(url);
    if (res.ok) {
      const data = await res.json();
      console.log(data)
      return data;
    } else {
      console.log("Can not get DATA!!!")
    }
  } catch (err) {
    console.log(err)
  }
}

const addPlayer = async (playerName, playerScore) => {
  try {
    const url = 'http://localhost:8080/api/players';

    if (!playerName || playerName.trim() === '') {
      playerName = 'Guest';
    }

    const playerData = {
      name: playerName,
      score: playerScore,
    };

    const res = await fetch(url, {
      method: "POST",
      headers: {
        "Content-Type": "application/json",
      },
      body: JSON.stringify(playerData),
    });

    if (res.status === 201) {
      console.log("Player data added successfully");
    } else {
      console.error("Failed to add player data. Server returned status:", res.status);
    }
  } catch (error) {
    console.error("An error occurred:", error);
  }
};

const showScore = () => {
  getAllPlayer()
  isScoreBoardVisible.value = true;
  scoreBoardData.value = scoreBoardData.value.slice(0, 10);
}

const hideScoreBoard = () => {
  isScoreBoardVisible.value = false;
}
</script>
<template>
  <div class="w-screen h-screen" style="background-color: #2A303C">
    <div class="w-full h-full" v-show="play">
      <div class="flex justify-center w-5/12 mx-auto">
        <!-- Logo -->
        <img src='./assets/Logo.png'>
      </div>

      <!-- หน้าแรก -->
      <div class="w-1/3 mx-auto mt-6 pb-16 rounded-3xl" style="background-color: #334155;">
        <div class="flex flex-col items-center">

          <!-- ใส่ชื่อ -->
          <input type="text" placeholder="Type your name..."
            class=" w-5/12 text-center mt-16 text-black placeholder-black rounded-md h-12"
            style="background-color: #A1A7B3;" v-model="name">

          <!-- ปุ่มเริ่มเกม -->
          <button class="w-5/12 h-12 mt-4 rounded-md text-white" style="background-color: #111B2E;" @click="start()">
            Let's Start!
          </button>

          <!-- เส้น -->
          <hr class="w-8/12 mt-10">

          <!-- วิธีเล่น -->
          <label for="my-modal" class="btn mt-10 w-5/12 text-white capitalize" style="background-color: #111B2E;">How to
            play</label>
          <input type="checkbox" id="my-modal" class="modal-toggle" />
          <div class="modal">
            <div class="modal-box">
              <h3 class="font-bold text-lg text-center">How to play</h3>
              <p class="py-4 text-center">Click on the different shade of color in a short period of time.
                Once you find the different shade,<br>
                you click it and score one point if it is the different one.</p>
              <div class="modal-action flex justify-center">
                <label for="my-modal" class="btn">OK</label>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>


    <!-- หน้าเล่นเกม  -->
    <div class="w-full h-full" v-show="game">
      <div class="w-full h-1/6">
        <div class="w-1/12 ml-6">

          <!-- โลโก้มุมซ้าย -->
          <img src="./assets/Logo.png" @click="mainmenu()">
        </div>
      </div>

      <!-- กึ่งกลาง -->
      <div class="flex flex-row">

        <!-- แสดงชื่อและคะแนน -->
        <div class="w-1/4 h-1/3 flex flex-col text-white">
          <!-- ชื่อ -->
          <p class="text-lg ml-auto">
            <span class="font-bold" v-if="name.length !== 0">
              Player: <span class="font-light">{{ name }} </span>
            </span>
            <span class="font-bold" v-if="name.length === 0">
              Player: <span class="font-light"> Guest </span>
            </span>
          </p>

          <!-- คะแนน -->
          <p class="text-lg ml-auto">
            <span class="font-bold">
              Score: <span class="font-light"> {{ comma(score) }} </span>
            </span>
          </p>
        </div>

        <!-- ตัวเกม -->
        <div class="w-2/4 h-full">
          <div class="w-2/3 h-auto rounded-3xl shadow-lg m-auto pb-12 px-2" style="background-color: #334155;">
            <div class="w-full h-full flex flex-col">
              <span class="countdown flex justify-center font-mono text-6xl mt-5">
                <p id="counterElement">
                  {{ counter }}
                </p>
              </span>

              <div class="grid gap-1 m-auto mt-10" :class="block()">
                <div v-for="(round, index) in numArr" :key="index">
                  <div class="rounded-full" :class="`${round} ${size()}`" @click="check(round)"></div>
                </div>
              </div>
            </div>
          </div>
          <div class="flex flex-row justify-center">
            <!-- ปุ่มกลับหน้าหลัก -->
            <button class="h-14 w-44 rounded-xl mx-3 mt-10 text-white" style="background-color: #AC9C48;"
              @click="mainmenu()">
              Main menu
            </button>

            <!-- ปุ่มเริ่มเกมใหม่ -->
            <button class="h-14 w-44 rounded-xl mx-3 mt-10 text-white" style="background-color: #AC9C48;"
              @click="restart()">
              Restart Game
            </button>
          </div>
        </div>
      </div>
    </div>

    <!-- หน้าสุดท้าย -->
    <div class="w-full h-screen" v-show="end">
      <div class="w-full h-1/6">
        <div class="mt-2 w-1/12 ml-6">
          <img src="./assets/Logo.png" @click="mainmenu()">
        </div>
      </div>
      <div class="w-1/3 mx-auto mt-6 pb-16 rounded-3xl" style="background-color: #334155;">
        <div class="flex flex-col items-center">
          <div class="mt-16 text-3xl font-semibold">
            <h3> Congratulations!</h3>
            <p class="text-2xl ml-auto text-center mt-10">
              <span class="font-bold" v-if="name.length !== 0">
                {{ name }}
              </span>
              <span class="font-bold" v-if="name.length === 0">
                Guest
              </span>
            </p>

            <p class="text-2xl ml-auto text-center mt-8">
              <span class="font-bold">
                {{ comma(score) }} point!!
              </span>
            </p>
          </div>
        </div>
      </div>

      <div class="flex flex-row justify-center">
        <!-- ปุ่มกลับหน้าหลัก -->
        <button class="h-14 w-44 rounded-xl mx-3 mt-10 text-white" style="background-color: #AC9C48;" @click="mainmenu()">
          Main menu
        </button>

        <button class="h-14 w-44 rounded-xl mx-3 mt-10 text-white" style="background-color: #AC9C48;"
          @click="showScore()">
          Score Board
        </button>


        <div class="score-board-popup flex flex-col" v-if="isScoreBoardVisible">
          <div class="score-board-content h-4/6 w-4/6 items-center text-center " style="background-color: #334155;">
            <h2 class="text-2xl font-bold mb-2">Score Board</h2>
            <ul>
              <!-- เพิ่มแถวหัวข้อข้างบน -->
              <li class="header-row">
                <div class="player-info text-lg font-semibold">
                  <div class="no">&nbsp;&nbsp; No.</div>
                  <div class="player-name">&nbsp;Name</div>
                  <div class="player-score">Score</div>
                  <div class="player-date">Date</div>
                </div>
              </li>

              <hr style="margin: 0 auto; width: 95%;">
              <!-- วนลูปแสดงข้อมูลผู้เล่น -->
              <li v-for="(player, index) in scoreBoardData" :key="index" class="text-sm m-2">
                <div class="player-info">
                  <div class="no">{{ index + 1 }}</div>
                  <div class="player-name">{{ player.name }}</div>
                  <div class="player-score">{{ player.score }}</div>
                  <div class="player-date">{{ formatDate(player.date) }}</div>
                </div>
              </li>
            </ul>
          </div>
          <!-- ปุ่มปิดด้านล่าง -->
          <button class="close-button bottom-button" @click="hideScoreBoard">Close</button>
        </div>


        <!-- ปุ่มเริ่มเกมใหม่ -->
        <button class="h-14 w-44 rounded-xl mx-3 mt-10 text-white" style="background-color: #AC9C48;" @click="start()">
          Restart Game
        </button>
      </div>
      <div style="pointer-events: none;">
        <img src="./assets/firework.gif" alt="firework" class="absolute top-0 left-0">
        <img src="./assets/firework.gif" alt="firework" class="absolute top-0 right-0">
        <img src="./assets/firework.gif" alt="firework" class="absolute bottom-0 left-0">
        <img src="./assets/firework.gif" alt="firework" class="absolute bottom-0 right-0">
      </div>
    </div>
  </div>
</template>
 
<style scoped>
.score-board-popup {
  position: fixed;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background-color: rgba(0, 0, 0, 0.5);
  display: flex;
  justify-content: center;
  align-items: center;
  z-index: 999;
}

.score-board-content {
  background-color: #fff;
  padding: 20px;
  border-radius: 10px;
  box-shadow: 0 0 10px rgba(0, 0, 0, 0.2);
}

.close-button {
  background-color: #AC9C48;
  color: white;
  border: none;
  border-radius: 5px;
  padding: 10px 20px;
  cursor: pointer;
  margin-top: 10px;
}

.player-info {
  display: flex;
  flex-direction: row;
  justify-content: space-between;
  align-items: center;
  padding: 5px;
  margin: 5px;
}

.player-name {
  flex: 1;
  margin-right: 10px;
}

.player-score {
  flex: 1;
  margin-right: 10px;
}

.player-date {
  flex: 1;
}

.no {
  flex: 1;
  margin-right: 10px;
}
</style>
