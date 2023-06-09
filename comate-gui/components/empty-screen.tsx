import { UseChatHelpers } from 'ai/react'

import { Button } from '@/components/ui/button'
import { ExternalLink } from '@/components/external-link'
import { IconArrowRight } from '@/components/ui/icons'

let demoUrl = `https://github.com/archguard/ddd-monolithic-code-sample`;
const exampleMessages = [
  {
    heading: 'First, import a system as context',
    message: `Introduce the system for following repository: ${demoUrl}`
  },
  {
    heading: 'Summarize the architecture',
    message: `Summarize the architecture for this system: ${demoUrl}`
  },
  {
    heading: 'Check REST API Specification',
    message: `Check REST API specification for this system: ${demoUrl}`
  },
  {
    heading: `Is this system follow foundation specification?`,
    message: `Is this system follow foundation specification? ${demoUrl}`
  }
]

export function EmptyScreen({ setInput }: Pick<UseChatHelpers, 'setInput'>) {
  return (
    <div className="mx-auto max-w-2xl px-4">
      <div className="rounded-lg border bg-background p-8">
        <h1 className="mb-2 text-lg font-semibold">
          Welcome to ArchGuard Co-mate - AI Copilot for your Architecture!
        </h1>
        <p className="mb-2 leading-normal text-muted-foreground">
          This is an open source AI tools built with{' '}
          <ExternalLink href="https://github.com/archguard/">ArchGuard Team</ExternalLink>
          .
        </p>
        <p className="leading-normal text-muted-foreground">
          You can start a conversation here or try the following examples:
        </p>
        <div className="mt-4 flex flex-col items-start space-y-2">
          {exampleMessages.map((message, index) => (
            <Button
              key={index}
              variant="link"
              className="h-auto p-0 text-base"
              onClick={() => setInput(message.message)}
            >
              <IconArrowRight className="mr-2 text-muted-foreground"/>
              {message.heading}
            </Button>
          ))}
        </div>
      </div>
    </div>
  )
}
