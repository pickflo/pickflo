/**
 * 
 */
document.addEventListener('DOMContentLoaded', () => {

	const layout=document.querySelector('div#layout');
	
	// 인기영화 DB data 불러오기
	const uri = `/api/popular/list`;
	axios
		.get(uri)
		.then((response) => {
			getMovies(response.data);
		})
		.catch((error) => {
			console.log(error);
		})

	function getMovies(data) {

		let htmlStr = '';
		for (let i = 0; i < data.length; i++) {
			const code = data[i].code;
			const title = data[i].title;
			const img = data[i].img;
			
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