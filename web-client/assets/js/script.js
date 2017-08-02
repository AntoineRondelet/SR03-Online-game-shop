// ------------------------------------------------------ //
//            Initialization/State of the app             //
// ------------------------------------------------------ //


// -- Variables that keep the state of the app -- //
var filters = {};
var pageNumber = 1;
var lastPage = 100;
var itemsPerPage = 10;

// -- Function that is ran when the DOM is loaded -- //
document.addEventListener('DOMContentLoaded', function() {
    // -- Initialization of the filters -- //
    initFilters();

    // -- Each time the URI changes, the appropriate page is rendered -- //
    window.onhashchange = function(){
        render(decodeURI(window.location.hash));
    }

    // -- Control if there is a token is sessionStorage -- //
    if (!sessionStorage.getItem('token')) {
        window.location.hash = '#login';
    } else {
        renderGames();
    }

    // -- Init events in different parts of the application -- //
    initEvents();
})


// -- initEvents initializes the event linked to different components of the app -- //
function initEvents() {
    // -- Init the event related to the "clean filters" button -- //
    var filtersCleanButton = document.getElementsByClassName('filters')[0].getElementsByTagName('button')[0];
    filtersCleanButton.addEventListener('click', function(e) {
        e.preventDefault();
        window.location.hash = '#';
    });

    // -- Init events of the single game page buttons -- //
    var singleGamePage = document.getElementsByClassName('single-game')[0];
    singleGamePage.addEventListener('click', function (e) {
        if (singleGamePage.classList.contains('visible')) {
            var clicked = e.target;

            // -- If the close button or the background are clicked render the previous page -- //
            if (clicked.classList.contains('close') || clicked.classList.contains('overlay')) {
                createQueryHash(filters);
            }
        }
    });
    
    // -- Init events of the cart page buttons -- //
    var cartPage = document.getElementsByClassName('cart')[0];
    cartPage.addEventListener('click', function (e) {
        if (cartPage.classList.contains('visible')) {
            var clicked = e.target;
            
            // -- If the close button or the background are clicked render the previous page -- //
            if (clicked.classList.contains('close') || clicked.classList.contains('overlay')) {
                createQueryHash(filters);
            }
        }
    });
    
    // -- Init events of the logout page buttons -- //
    var logoutPage = document.getElementsByClassName('logout')[0];
    logoutPage.addEventListener('click', function (e) {
        if (logoutPage.classList.contains('visible')) {
            var clicked = e.target;

            // -- If the close button or the background are clicked render the previous page -- //
            if (clicked.classList.contains('close') || clicked.classList.contains('overlay')) {
                window.location.hash = '#';
            }
        }
    });

    // -- Init logout button's event in header -- //
    var logout = document.getElementById('logout-button');
    logout.addEventListener('click', function (e) {
        e.preventDefault();
        window.location.hash = '#logout';
    });

    // -- Init cart button's event in header -- //
    var cart = document.getElementById('cart-button');
    cart.addEventListener('click', function (e) {
        e.preventDefault();
        window.location.hash = '#cart';
    });
    
    // -- Init pay button's event in cart page -- //
    var pay = document.getElementById('pay-button');
    pay.addEventListener('click', function (e) {
        e.preventDefault();
        validateCart();
        window.location.hash = '#';
    });
}

function initFilters() {
    initPagination();
    initFilterCheckboxes();
}

function initPagination() {
    var paginationCriterias = document.querySelectorAll('.pageNumber li');
    paginationCriterias.forEach(function (element) {
        element.addEventListener('click', function(e) {
            
            var filterName = "page";

            // If the filter for this specification isn't created yet - do it.
            if(!(filters[filterName] && filters[filterName].length)){
                filters[filterName] = [];
            } else {
                filters[filterName].length = 0;
            }

            // -- Calculate on which page number we should go -- /
            var pageToGo = element.innerHTML;
            var currentPage = parseInt(document.getElementById("currentPage").innerHTML);
            if (pageToGo == "«") {
                if (currentPage > 1) {
                    pageToGo = currentPage - 1;
                } else {
                    pageToGo = currentPage;
                }
            } else if (pageToGo == "»") {
                if (currentPage < lastPage) {
                    pageToGo = currentPage + 1;
                } else {
                    pageToGo = currentPage;
                }
            } else {
                pageToGo = currentPage;
            }

            //  Push values into the chosen filter array
            filters[filterName].push(pageToGo);
        
            // Change the url hash;
            createQueryHash(filters);
        })
    })

    var itemsPerPage = document.querySelectorAll('.itemsPerPage li');
    itemsPerPage.forEach(function (element) {
        element.addEventListener('click', function(e) {

            var filterName = "itemsPerPage";

            // If the filter for this specification isn't created yet - do it.
            if(!(filters[filterName] && filters[filterName].length)){
                filters[filterName] = [];
            } else { // If the filter "itemsPerPage already had a value, we clean it an put the new value inside (we can only have 1 value for the number of items in this array)
                filters[filterName].length = 0;
            }

            //  Push values into the chosen filter array
            filters[filterName].push(element.innerHTML);
            
            // Change the url hash;
            createQueryHash(filters);
        })
    })
}
    
function initFilterCheckboxes() {
    // Set an eventListener on all checkboxes that are part of the criterias
    var filterCheckboxes = document.querySelectorAll('.filter-criteria input[type="checkbox"]');
    filterCheckboxes.forEach(function (element) {
        element.addEventListener('click', function(e) {

            // -- Get the filterName -- //
            var filterName = element.getAttribute("name");

            // -- We click on the element in order to check it: Add a filter -- //
            if(element.checked) {

                // -- If the filter for this specification isn't created yet - do it. -- //
                if(!(filters[filterName] && filters[filterName].length)){
                    filters[filterName] = [];
                }

                // -- Push values into the chosen filter array -- //
                filters[filterName].push(element.value);

            } else { // -- We click on the checkbox in order to remove a filter -- //
                if(filters[filterName] && filters[filterName].length && (filters[filterName].indexOf(element.value) != -1)) {
                    // -- Find the checkbox value in the corresponding array inside the filters object. -- //
                    var index = filters[filterName].indexOf(element.value);
                    
                    // -- Remove the filter -- //
                    filters[filterName].splice(index, 1);
                    
                    // -- If it was the last remaining value for this specification, delete the whole array -- // 
                    if(!filters[filterName].length){
                        delete filters[filterName];
                    }
                }
            }

        // -- Change the url hash -- //
        createQueryHash(filters);
        })
    })
}

// ------------------------------------------------------ //
//                  Cart management                       //
// ------------------------------------------------------ //

// --  dropCart drops the content of the cart is the user logs out without paying -- //
function dropCart() {
    // -- Get the token stored in sessionStorage -- //
    var token = sessionStorage.getItem('token');
    
    // -- Get the cartId if it exists -- //
    var cartId = '-1';
    if (sessionStorage.getItem('cartId')) {
        cartId = sessionStorage.getItem('cartId');
    } else {
        return;
    }
    
    // -- Initialize ajax request object -- //
    var xhr = new XMLHttpRequest();
    xhr.open("GET", "http://localhost:8080/gameshop_war_exploded/api/cart/drop/" + cartId, true);
    xhr.setRequestHeader('Authorization','Bearer ' + token);
    xhr.setRequestHeader("Content-Type", "application/json");
    xhr.onreadystatechange = function () {
        // -- Once we get the response from the server, we check the http code -- //
        if (xhr.readyState == XMLHttpRequest.DONE) {
            if (xhr.status != 200 && xhr.status != 206) {
                if (xhr.status == 401) {
                    // -- "Redirect" to the login page -- //
                    window.location.hash = '#login';
                } else {
                    // -- Show error in alert-- //
                    alert("Oops ! Something went wrong :(");
                }
                return;
            }
        }
    };
    xhr.send();
}

// --  validateCart allow the user to pay the games he stored in his cart, and confirm the purchase-- //
function validateCart() {
    // -- Get the token stored in sessionStorage -- //
    var token = sessionStorage.getItem('token');
    
    // -- Get the cartId if it exists -- //
    var cartId = '-1';
    if (sessionStorage.getItem('cartId')) {
        cartId = sessionStorage.getItem('cartId');
    }

    // -- Initialize ajax request object -- //
    var xhr = new XMLHttpRequest();
    xhr.open("GET", "http://localhost:8080/gameshop_war_exploded/api/cart/validate/" + cartId, true);
    xhr.setRequestHeader('Authorization','Bearer ' + token);
    xhr.setRequestHeader("Content-Type", "application/json");
    xhr.onreadystatechange = function () {
        // -- Once we get the response from the server, we check the http code -- //
        if (xhr.readyState == XMLHttpRequest.DONE) {
            if (xhr.status != 200 && xhr.status != 206) {
                if (xhr.status == 401) {
                    // -- "Redirect" to the login page -- //
                    window.location.hash = '#login';
                } else {
                    // -- Show error in alert -- //
                    alert("Oops ! Something went wrong :(");
                }
                return;
            }

            var response = JSON.parse(xhr.responseText);
            alert(response["message"]);
            if (sessionStorage.getItem('cartId')) {
                sessionStorage.removeItem('cartId');
            }

            // -- Flush the content of the cart page list -- //
            var cartGameListTemplate = document.getElementsByClassName("cart-games-list")[0];
            while (cartGameListTemplate.firstChild) {
                cartGameListTemplate.removeChild(cartGameListTemplate.firstChild);
            }

            // Reset the Pay button value
            setPayButtonValue();
        }
    };
    xhr.send();
}

function buyAGame(gameId) {
    // -- Get the token stored in sessionStorage -- //
    var token = sessionStorage.getItem('token');
    
    // -- Get the cartId if it exists -- //
    var cartId = '-1';
    if (sessionStorage.getItem('cartId')) {
        cartId = sessionStorage.getItem('cartId');
    }
    
    // -- Initialize ajax request object -- //
    var xhr = new XMLHttpRequest();
    xhr.open("PUT", "http://localhost:8080/gameshop_war_exploded/api/purchases", true);
    xhr.setRequestHeader('Authorization','Bearer ' + token);
    xhr.setRequestHeader("Content-Type", "application/json");
    xhr.onreadystatechange = function () {
        // -- Once we get the response from the server, we check the http code -- //
        if (xhr.readyState == XMLHttpRequest.DONE) {
            if (xhr.status != 200 && xhr.status != 206) {
                if (xhr.status == 401) {
                    // -- "Redirect" to the login page -- //
                    window.location.hash = '#login';
                } else {
                    // -- Show error page -- //
                    alert("Oops ! Something went wrong :(");
                }
                return;
            }

            // -- Set the cart ID -- //
            var response = JSON.parse(xhr.responseText);
            sessionStorage.setItem("cartId", response["cart"]);
            alert(response["message"]);
        }
    };
    xhr.send(JSON.stringify({
        cart: cartId,
        game: gameId,
        action: 'add'
    }));
}

function deleteAGame(gameId) {
    // -- Get the token stored in sessionStorage -- //
    var token = sessionStorage.getItem('token');
    
    // -- Get the cartId if it exists -- //
    var cartId = '-1';
    if (sessionStorage.getItem('cartId')) {
        cartId = sessionStorage.getItem('cartId');
    }
    
    // -- Initialize ajax request object -- //
    var xhr = new XMLHttpRequest();
    xhr.open("PUT", "http://localhost:8080/gameshop_war_exploded/api/purchases", true);
    xhr.setRequestHeader('Authorization','Bearer ' + token);
    xhr.setRequestHeader("Content-Type", "application/json");
    xhr.onreadystatechange = function () {
        // -- Once we get the response from the server, we check the http code -- //
        if (xhr.readyState == XMLHttpRequest.DONE) {
            if (xhr.status != 200 && xhr.status != 206) {
                if (xhr.status == 401) {
                    // -- "Redirect" to the login page -- //
                    window.location.hash = '#login';
                } else {
                    // -- Show error in alert -- //
                    alert("Oops ! Something went wrong :(");
                }
                return;
            }

            // -- Set the cart ID -- //
            var response = JSON.parse(xhr.responseText);
            sessionStorage.setItem("cartId", response["cart"]);
            alert(response["message"]);
        }
        return;
    };
    xhr.send(JSON.stringify({
        cart: cartId,
        game: gameId,
        action: 'delete'
    }));
}

function cleanFilters() {
    var filterCheckboxes = document.querySelectorAll('.filter-criteria input[type="checkbox"]');
    filterCheckboxes.forEach(function (element) {
        element.checked = false;
    })
}

function calculateCartPrice(cartComposition) {
    var price = 0;
    cartComposition.games.forEach(function(game){
        var gamePrice = parseInt(game.game_price);
        var gameInstances = parseInt(game.instances.length);
        price += gamePrice * gameInstances;
    })

    return price;
}

function setCartButtonValue(value) {
    var cartButton = document.getElementById("cart-button");
    cartButton.innerHTML = "Cart (" + value + "$)";
}

function setPayButtonValue(value) {
    value = value || "0";
    var payButton = document.getElementById("pay-button");
    payButton.innerHTML = "Pay (" + value + "$)";
}

// -- Renders the cart page -- //
function renderCartPage(){

    // -- Get the token stored in sessionStorage -- //
    var token = sessionStorage.getItem('token');

    var cartId = '-1';
    // -- If the cartId does not exist yet -> render empty cart page -- //
    if (!sessionStorage.getItem('cartId')) {
        var pageCart = document.getElementsByClassName('cart')[0];
        pageCart.classList.add('visible');

        // Render empty page and return to avoid the useless ajax call
        return; 
    }

    // -- Flush the content of the cart page list-- //
    var gameListTemplate = document.getElementsByClassName("cart-games-list")[0];
    while (gameListTemplate.firstChild) {
        gameListTemplate.removeChild(gameListTemplate.firstChild);
    }
   
    // -- Retrieve the cartId if it exists -- //
    cartId = sessionStorage.getItem('cartId');

    // -- Initialize ajax request object -- //
    var xhr = new XMLHttpRequest();
    xhr.open("GET", "http://localhost:8080/gameshop_war_exploded/api/purchases/" + cartId, true);
    xhr.setRequestHeader('Authorization','Bearer ' + token);
    xhr.onreadystatechange = function () {
        // -- Once we get the response from the server, we check the http code -- //
        if (xhr.readyState == XMLHttpRequest.DONE) {
            if (xhr.status != 200 && xhr.status != 206) {
                if (xhr.status == 401) {
                    // -- "Redirect" to the login page -- //
                    window.location.hash = '#login';
                } else {
                    // -- Show error page -- //
                    alert("Oops HER ! Something went wrong :(")
                }
                return;
            }


            // -- Test if the response is empty before processing -- //
            if (xhr.responseText == "") {
                return
            }

            // -- If the server responded with a 200, we process the data and render the page -- //
            var cartComposition = JSON.parse(xhr.responseText);

            // -- Set the total Price of the cart -- //
            var totalPrice = calculateCartPrice(cartComposition);
            setPayButtonValue(totalPrice);

            // -- Diplay each game of the cart -- //
            cartComposition.games.forEach(function(game) {
                var template = document.getElementById("cart-games-template").innerHTML;
                // -- Replace placeholders with values from game object in the template -- //
                for (var key in game) {
                    template = template.replace(new RegExp("{{"+key+"}}", 'g'), game[key]);
                }

                // -- Calculate the number of instances of each game bought by the user -- //
                template = template.replace(new RegExp("{{entities}}", 'g'), game.instances.length);
                
                // -- Calculate the price: game_price * number of instances for each game -- //
                template = template.replace(new RegExp("{{price}}", 'g'), game.instances.length * game.game_price);

                // -- Add the game to the list -- //
                var element = document.createElement('li');
                element.innerHTML = template;

                // -- Add an eventListener to the new element -- //
                var deleteButton = element.getElementsByTagName('button')[0];
                deleteButton.addEventListener('click', function(e) {
                    var idGame = deleteButton.getAttribute("data-index");
                    deleteAGame(idGame);
                    renderCartPage();
                });

                document.getElementsByClassName("cart-games-list")[0].appendChild(element);
                })
            }

        // -- Display the page with the data -- //
        var pageCart = document.getElementsByClassName('cart')[0];
        pageCart.classList.add('visible');

        return;
    };
    xhr.send();
}


// ------------------------------------------------------ //
//              Games list page management                //
// ------------------------------------------------------ //

function renderGames(queryString) {
    // -- Get the token stored in sessionStorage -- //
    var token = sessionStorage.getItem('token');
   
    // Set the queryString to the empty string if it is not given
    queryString = queryString || "";

    // -- Flush the content of the main page list -- //
    var gameListTemplate = document.getElementsByClassName("games-list")[0];
    while (gameListTemplate.firstChild) {
        gameListTemplate.removeChild(gameListTemplate.firstChild);
    }

    // -- Initialize ajax request object -- //
    var xhr = new XMLHttpRequest();
    xhr.open("GET", "http://localhost:8080/gameshop_war_exploded/api/games" + queryString, true);
    xhr.setRequestHeader('Authorization','Bearer ' + token);
    xhr.onreadystatechange = function () {
        // -- Once we get the response from the server, we check the http code -- //
        if (xhr.readyState == XMLHttpRequest.DONE) {
            if (xhr.status != 200 && xhr.status != 206) {
                if (xhr.status == 401) {
                    // -- "Redirect" to the login page -- //
                    window.location.hash = '#login';
                } else {
                    // -- Show error page -- //
                    alert("Oops ! Something went wrong :(")
                }
                return;
            }
            // -- If the server responded with a 200, we process the data and render the page -- //

            var gameList = JSON.parse(xhr.responseText);

            // -- Set pagination criterias -- //
            lastPage = gameList.pagination.pages.last;
            pageNumber = gameList.pagination.current_page;
            document.getElementById('currentPage').innerHTML = pageNumber;

            // -- Fill template with data -- //
            gameList.list.forEach(function(game) {
                var template = document.getElementById("games-template").innerHTML;
                // -- Replace placeholders with values from game object in the template -- //
                for (var key in game) {
                    template = template.replace(new RegExp("{{"+key+"}}", 'g'), game[key]);
                }

                // -- Build rating -- //
                template = template.replace(new RegExp("{{rate-percent}}", 'g'), (game["rate"]/5) * 100);
                
                // -- Build type -- //
                var type = "";
                game.keywords.forEach(function(keyword) {
                    type = type + " " + keyword.word;
                })
                template = template.replace(new RegExp("{{type}}", 'g'), type);
                
                // -- Build images -- //
                template = template.replace(new RegExp("{{image}}", 'g'), "assets/images/list/"+game["id"]+".jpg");
                // -- Add the game to the list -- //
                var element = document.createElement('li');
                element.innerHTML = template;

                // -- If the game we are dealing with is not in stock anymore: hide it -- //
                if (game["stock"] <= 0) {
                    element.classList.add('hidden');
                } 

                // -- Add an eventListener to the new element -- //
                element.addEventListener('click', function(e) {
                    var clicked = e.target;
                    if (clicked.classList.contains('buy')) {
                        var idGame = element.getElementsByClassName("game")[0].getAttribute("data-index");
                        buyAGame(idGame);
                    } else {
                        e.preventDefault();
                        window.location.hash = 'game/' + game["id"];
                    }
                })

                document.getElementsByClassName("games-list")[0].appendChild(element);
            })  
        }
        // -- Display the page with the data -- //
        document.getElementsByClassName('all-games')[0].classList.add('visible');
    };
    xhr.send();
}

// ------------------------------------------------------ //
//                 Login page management                  //
// ------------------------------------------------------ //

// -- Shows the login page if the user's credentials are not valid -- //
function renderLoginPage(){
    // -- Render the login form -- //
    var pageLogin = document.getElementsByClassName('login')[0];
    pageLogin.classList.add('visible');

    // -- Disable the CART and Logout buttons -- //
    // var menu = document.getElementsByClassName('menu')

    var form = document.forms.namedItem("login-form");

    // -- Listen submit on submit button -- //
    form.addEventListener('submit', function(ev) {
        var username = document.querySelectorAll("input[type=text]")[0].value;
        var password = document.querySelectorAll("input[type=password]")[0].value;

        // -- Prepare ajax request -- //
        xhr = new XMLHttpRequest();
        xhr.open('POST', 'http://localhost:8080/gameshop_war_exploded/api/login');
        xhr.setRequestHeader('Content-Type', 'application/x-www-form-urlencoded');
        xhr.onreadystatechange = function() {
            if (xhr.readyState == XMLHttpRequest.DONE) {
                if (xhr.status != 200) {
                    if (xhr.status == 401) {
                        document.getElementById('message-login').innerHTML = "Bad credentials";
                        return
                    } else {
                        document.getElementById('message-login').innerHTML = "Something went wrong; try again " + "(error code " + xhr.status + ")"; 
                        return
                    }
                }
                var response = JSON.parse(xhr.responseText);
                // --  The user is logged in. Retrieve the token and store it in sessionStorage -- //
                var token = response["token"];
                sessionStorage.setItem("token", token);

                // -- Render the home page -- //
                window.location.hash = '#';
            }
        }

        // -- Send data through POST method to authenticate the user -- //
        var data = 'username=' + username + '&password=' + password;
        xhr.send(encodeURI(data));
        ev.preventDefault();
    })
}


// ------------------------------------------------------ //
//                 Error page management                  //
// ------------------------------------------------------ //

// -- Renders the error page -- //
function renderErrorPage(){
    var pageError = document.getElementsByClassName('error')[0];
    pageError.classList.add('visible');
}

// ------------------------------------------------------ //
//            Single game  page management                //
// ------------------------------------------------------ //

// -- Renders the single game page -- //
function renderSingleGamePage(gameId){
    // -- Get the token stored in sessionStorage -- //
    var token = sessionStorage.getItem('token');
    
    // -- Initialize ajax request object -- //
    var xhr = new XMLHttpRequest();
    xhr.open("GET", "http://localhost:8080/gameshop_war_exploded/api/games/" + gameId, true);
    xhr.setRequestHeader('Authorization','Bearer ' + token);
    xhr.onreadystatechange = function () {
        // -- Once we get the response from the server, we check the http code -- //
        if (xhr.readyState == XMLHttpRequest.DONE) {
            if (xhr.status != 200 && xhr.status != 206) {
                if (xhr.status == 401) {
                    // "Redirect" to the login page
                    window.location.hash = '#login';
                } else {
                    // Show error page
                    alert("Oops ! Something went wrong :(")
                }
                return;
            }

            // -- If the server responded with a 200, we process the data and render the page -- //
            var gameDescription = JSON.parse(xhr.responseText);
            var content = document.getElementsByClassName('single-game')[0];
            content.getElementsByTagName('h3')[0].innerHTML = gameDescription["title"];
            content.getElementsByTagName('img')[0].setAttribute("src", "assets/images/preview/" + gameDescription["id"] + ".jpg");
            content.getElementsByTagName('p')[0].innerHTML = gameDescription["description"];
        }

        // -- Display the page with the data -- //
        var pageSingleGame = document.getElementsByClassName('single-game')[0].classList.add('visible');
    };
    xhr.send();
}



// ------------------------------------------------------ //
//         Management of the user's navigation            //
// ------------------------------------------------------ //

// -- Renders the appropriate page while we navigate -- //
function render(url) {
    // -- Get the keyword from the url -- //
    var temp = url.split('/')[0];

    var pages = document.getElementsByClassName('page');
    for(var i = 0, len = pages.length; i < len; i++) {
        pages[i].classList.remove('visible');
    }

    document.getElementsByClassName('main-content')[0].classList.remove('visible');

    var	map = {
        // The "Homepage".
        '': function() {
            console.log("Rendering home page");

            // -- We reset the filters to nothing -- //
            filters = {};

            // -- We uncheck all the filter's checkboxes -- //
            cleanFilters();

            // -- We render the home page -- //
            renderGames();
        },
        
        // Renders the single game page
        '#game': function() {
            console.log("Rendering single game page");

            // -- Retrieving the game id from the URL -- //
            var gameId = url.split('#game/')[1].trim();

            // -- Rendering the game's description page -- //
            renderSingleGamePage(gameId);
        },

        // Renders login page
        '#login': function() {
            console.log("Rendering login page");
            renderLoginPage();
        },
        
        // Renders login page
        '#logout': function() {
            console.log("Rendering logout page");
            
            // -- Removing user's cart when logging out without buying -- //
            dropCart();
            if (sessionStorage.getItem('cartId')) {
                sessionStorage.removeItem('cartId');
            }
            renderLogoutPage();
        },

        // Renders cart page
        '#cart': function() {
            console.log("Rendering cart page");
            renderCartPage();
        },
        
        // Page with filtered games
        '#filter': function() {
            // -- Getting the filters from the URL -- //
            url = url.split('#filter/')[1].trim();

            // -- Trying to parse the filters object from the query string -- //
            try {
                filters = JSON.parse(url);
            } catch(err) {
                // -- If filters are not valid (parsing fails) go to home page -- //
                window.location.hash = '#';
                return;
            }
            renderFilterResults(filters);
        },
    };
    // -- Execute the appropriate rendering function based on the URL -- //
    if (map[temp]) {
        map[temp]();
    } else {
        // -- If we call a rendering function that does not exist: render error page -- //
        renderErrorPage();
    }
}

// ------------------------------------------------------ //
//              Management of the filters                 //
// ------------------------------------------------------ //

// -- createQueryHash gets the filters object, turns it into a string and writes it into the hash -- //
function createQueryHash(filters){
    // -- Checks if the filters object is empty -- //
    if(!(Object.keys(filters).length === 0 && filters.constructor === Object)){
        window.location.hash = '#filter/' + JSON.stringify(filters);
    } else {
        window.location.hash = '#';
    }
}

// -- renderFilterResults turns the filters object into a valid query understandable by the API -- //
function renderFilterResults(filters) {
    // -- Beginning of a query in URI
    var uri = '?';
    var query = [];

    // -- Retrieve the fields of the JSON object -- //
    var keys = Object.keys(filters);

    // -- Examine filters values -- //
    for (var i = 0; i < keys.length; i++) {
        var filterKey = keys[i];
        var filterValues = filters[filterKey];
        for (var j = 0; j < filterValues.length; j++) {
            var filterValue = filterValues[j];
            query.push(filterKey + "=" + filterValue);
        }
    }

    // -- Join all the key=value query params into a string like: ?key=value&key2=value2 -- //
    uri = uri + query.join('&');

    // -- Do the ajax call using the generated URL and FETCH the game list template before adding the new data -- //
    renderGames(uri);
}

// ------------------------------------------------------ //
//                Logout page management                  //
// ------------------------------------------------------ //

// -- Renders the logout page -- //
function renderLogoutPage(){
    // -- Get the token stored in sessionStorage -- //
    var token = sessionStorage.getItem('token');

    // -- Initialize ajax request object -- //
    var xhr = new XMLHttpRequest();
    xhr.open("GET", "http://localhost:8080/gameshop_war_exploded/api/logout", true);
    xhr.setRequestHeader('Authorization','Bearer ' + token);
    xhr.onreadystatechange = function () {
        // -- Once we get the response from the server, we check the http code -- //
        if (xhr.readyState == XMLHttpRequest.DONE) {
            if (xhr.status != 200 && xhr.status != 206) {
                if (xhr.status == 401) {
                    // "Redirect" to the login page
                    window.location.hash = '#login';
                } else {
                    // Show error page
                    alert("Oops HER ! Something went wrong. Could not logged out. Please close your windows and you session will be destroyed.")
                }
                return;
            }
        }

        // -- Display the page with the data -- //
        var pageLogout = document.getElementsByClassName('logout')[0];
        pageLogout.classList.add('visible');
    };
    xhr.send();
}
