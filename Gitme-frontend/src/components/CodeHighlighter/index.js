import SyntaxHighlighter from 'react-syntax-highlighter'
import { githubGist } from 'react-syntax-highlighter/dist/esm/styles/hljs'
import { filesize } from "filesize"

export default function CodeHighlighter({
	fileName,
	content,
	linesNum,
	size,
}) {
	const language = fileName.match(/.(\w+)$/)?.[1]?.toLowerCase()
	return <div className='border border-gray-300 overflow-hidden rounded'>
		<div className='px-4 py-2 flex space-x-3 bg-gray-100 border-b border-gray-300'>
			<div className='font-mono text-sm text-gray-600'>{linesNum.toLocaleString('en')} lines</div>
			<div className='font-mono text-sm text-gray-600'>{filesize(size)}</div>
		</div>
		<div className='px-3 pt-2 pb-4 text-sm'>
		<SyntaxHighlighter language={language} style={githubGist}
			showLineNumbers={true}
		>
			{content}
		</SyntaxHighlighter>
		</div>
	</div>
}