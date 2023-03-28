import ReactMarkdown from "react-markdown";
import remarkGfm from "remark-gfm";
import { Prism as SyntaxHighlighter } from "react-syntax-highlighter"
import { materialLight } from 'react-syntax-highlighter/dist/esm/styles/prism'

export default function MarkdownViewer({ md, className = '' }) {
	return <article className={"prose max-w-none " + className}>
		<ReactMarkdown children={md} remarkPlugins={[remarkGfm]}
			components={{
				code({node, inline, className, children, ...props}) {
					const match = /language-(\w+)/.exec(className || '')
					return !inline && match ? (
						<SyntaxHighlighter
							children={String(children).replace(/\n$/, '')}
							style={materialLight}
							language={match[1]}
							PreTag="div"
							className="rounded overflow-hidden"
							{...props}
						/>
					) : (
						<code className={className} {...props}>
							{children}
						</code>
					)
				}
			}}
		/>
	</article>
}