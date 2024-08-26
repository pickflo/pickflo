/**
 * 
 */
document.addEventListener('DOMContentLoaded', () => {

	const layout=document.querySelector('div#layout');
	const uri = `/api/popular/list`;
	console.log('@@@@@@@@@@@@@@@@@@@@@@@@');
	axios
		.get(uri)
		.then((response) => {
			getMovies(response.data);
			console.log('야호야호야호~~~~');
		})
		.catch((error) => {
			console.log('노노논노노ㅗㄴ~~~~');
			
			console.log(error);
		})

	function getMovies(data) {

		let htmlStr = '';
		for (let i = 0; i < data.length; i++) {
			const code = data[i].code;
			const title = data[i].title;
			const img = data[i].img;
			console.log(`---------------title=${title}`);
			
			htmlStr += `
				<div class="movie">
					<div class="photo">
						<a href="/movie/details/${code}">
							<img src="${img}" alt="${title}">
						</a>
					</div>
				</div>
			`;
		}
		layout.insertAdjacentHTML('beforeend', htmlStr);
		
	}
})