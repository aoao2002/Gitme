export function join(...args) {
	let path = ''
	for (let arg of args) {
		if (!arg) continue
		arg = arg.replace(/\/+$/, '')
		if (arg === '') continue
		if (path) {
			path = path + '/' + arg
		}	else {
			path = arg
		}
	}
	return path
}

export function parent(path) {
	path = path.replace(/\/+$/, '')
	if (path === '') return ''
	const idx = path.lastIndexOf('/')
	if (idx < 0) return ''
	else return path.slice(0, idx)
}

export function trim(path) {
	path = path.replace(/^\/+/, '')
	path = path.replace(/\/+$/, '')
	return path
}