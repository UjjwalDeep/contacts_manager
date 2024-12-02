console.log("script loaded");

let currentTheme = getTheme();
console.log(currentTheme);

changeTheme();

function changeTheme(){

    document.querySelector("html").classList.add(currentTheme);

    const changeThemeBtn = document.querySelector('#theme_change_btn');

    changeThemeBtn.querySelector('span').textContent = currentTheme == "light" ? "Dark" : "Light";

    changeThemeBtn.addEventListener('click',(event) => {
      console.log("change theme button clicked");

      const oldTheme = currentTheme;

          if(currentTheme == "light"){
              currentTheme = "dark";
          }else{
              currentTheme = "light";
          }

          setTheme(currentTheme);

          document.querySelector('html').classList.remove(oldTheme);
          document.querySelector("html").classList.add(currentTheme);

          changeThemeBtn.querySelector('span').textContent = currentTheme == "light" ? "Dark" : "Light";

    });


}

function setTheme(theme){
localStorage.setItem("theme",theme);
}

function getTheme(){
   let theme =  localStorage.getItem("theme");

   if(theme)
    return theme;
   else
    return "light";
}

